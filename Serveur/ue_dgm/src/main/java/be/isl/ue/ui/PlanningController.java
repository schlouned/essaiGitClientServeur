/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.ui;

import be.isl.ue.business.PlanningFacade;
import be.isl.ue.entity.Planning;
import be.isl.ue.vm.PlanningSearchVM;

import java.io.Serializable;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

@Named("planningController")
@SessionScoped

public class PlanningController extends AbstractController<Planning, PlanningFacade, PlanningSearchVM>
        implements Serializable {

    @Inject
    private PlanningFacade ejbFacade;

    private List<Planning> items = null;
    private Planning selected;

    private Date scheduleInitialDate;

    private ScheduleModel eventModel;
    private PlanningScheduleEvent event = new PlanningScheduleEvent();

    public PlanningController() {
    }

    @PostConstruct
    public void init() {
        setSearch(new PlanningSearchVM());
        eventModel = new DefaultScheduleModel();

        items = ejbFacade.findAll();
        items.forEach((o) -> {
            eventModel.addEvent(new PlanningScheduleEvent(o));
        });
    }

    @Override
    public PlanningFacade getFacade() {
        return ejbFacade;
    }

    @Override
    public Planning create() {
        return new Planning();
    }

    public Planning getPlanning(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    public Date getScheduleInitialDate() {
        return scheduleInitialDate;
    }

    public void setScheduleInitialDate(Date scheduleInitialDate) {
        this.scheduleInitialDate = scheduleInitialDate;
    }

    // <editor-fold defaultstate="collapsed" desc="Schedule related methods"> 
    // 
    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public PlanningScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(PlanningScheduleEvent event) {
        this.event = event;
    }

    public void addEvent() {
        System.out.println("addEvent");
        if (event.getId() == null) {
            eventModel.addEvent(event);
        } else {
            event.setStartDate(event.convertToLocalDateTime(((Planning) event.getData()).getSeanceDate()));
            // event.setEndDate(event.setStartDate().plusHours(12));
            eventModel.updateEvent(event);
            ejbFacade.edit((Planning) event.getData());
        }
        event = new PlanningScheduleEvent();
    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (PlanningScheduleEvent) selectEvent.getObject();
    }

    public void onDateSelect(SelectEvent selectEvent) {
        event = new PlanningScheduleEvent("",
                (Date) selectEvent.getObject(),
                (Date) selectEvent.getObject());
    }

    public void onEventMove(ScheduleEntryMoveEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
        Planning o = (Planning) event.getScheduleEvent().getData();
        Calendar cal = Calendar.getInstance();

        cal.setTime(o.getSeanceDate());
        cal.add(Calendar.DAY_OF_YEAR, event.getDayDelta());
        o.setSeanceDate((cal.getTime()));
        ejbFacade.edit(o);
        addMessage(message);
    }

    public void onEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Event resized", "Day delta:" + event.getDayDeltaStart()
                + ", Minute delta:" + event.getMinuteDeltaStart());

        addMessage(message);
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    // </editor-fold>
    @Override
    public Planning getSelected() {
        return selected;
    }

    @Override
    public void setSelected(Planning selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }
    //***
    // new FindAll method
    private Class<Planning> entityClass;

    public List<Planning> findAll() {
        return this.getFacade().findAll();
    }

    //***
    public void createPlanning() {
        this.getFacade().insertOUeIntoPlanning(this.getSearch());

    }
    
    public void deletePlanning(){
       if (this.selected.getPresences().size()>0); 
          this.doDelete();
     
    }

  //for the dynamic list of academic years
    private String academicOption;

    public String getAcademicOption() {
        return academicOption;
    }

    public void setAcademicOption(String academicOption) {
        this.academicOption = academicOption;
    }

    private List<String> academicOptions;

    public List<String> getAcademicOptions() {
         academicOptions = ejbFacade.SelectAcademicYears(this.getSearch());
        return academicOptions;
    }

    public void setOptions(List<String> academicOptions) {
        this.academicOptions = academicOptions;
    }
}

final class PlanningScheduleEvent extends DefaultScheduleEvent {

    private Planning o;

    public PlanningScheduleEvent(Planning o) {
        this.o = o;
        Calendar calDate = Calendar.getInstance();
        calDate.setTime(o.getSeanceDate());

        super.setStartDate(convertToLocalDateTime(calDate.getTime()));
        calDate.add(Calendar.HOUR, 12);

        super.setEndDate(convertToLocalDateTime(calDate.getTime()));
        super.setTitle(o.toString());

        super.setData(o);
    }

    PlanningScheduleEvent() {
        o = new Planning();
        super.setData(o);
    }

    PlanningScheduleEvent(String string, Date startDate, Date endDate) {
        o = new Planning();
        o.setSeanceDate(startDate);
        super.setStartDate(convertToLocalDateTime(startDate));
        super.setEndDate(convertToLocalDateTime(endDate));

        super.setData(o);
    }

    public LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public Planning getPlanning() {
        return o;
    }

    public void setPlanning(Planning o) {
        this.o = o;
    }

}
