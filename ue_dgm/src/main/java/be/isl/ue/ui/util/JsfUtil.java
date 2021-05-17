package be.isl.ue.ui.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

public class JsfUtil {

    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    public static void addErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }

    //add common methods
    //*******************************************************************************************
    //Method to convert an academic year in two dates start and end
    //I decide to create this method because I had to do this code several times inthe program
    //so it was better to create a function to avoid to repeat code
    //*******************************************************************************************
    public static ArrayList<Date> convertAcademicYearToStartAndEndDate(String academicYear) throws ParseException {
        ArrayList<Date> l = new ArrayList();

        if (academicYear != null && !academicYear.equalsIgnoreCase("---")) {
            String str = academicYear;
            str = str.substring(0, 4);

            Integer endAcademic = Integer.parseInt(str);
            endAcademic++;
            String endDateAcademicYearString = endAcademic.toString();

            String startDateAcademicYearString = str + "-09-01";
            endDateAcademicYearString = endAcademic + "-06-30";

            Date startAcademicYear = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(startDateAcademicYearString).getTime());
            Date endAcademicYear = new Date(new SimpleDateFormat("yyyy-MM-dd").parse(endDateAcademicYearString).getTime());

            l.add(startAcademicYear);
            l.add(endAcademicYear);
            
        }
        else{
            l.add(new Date());
            l.add(new Date());
        }
        return l;
        
    }
    
    //********************************************
    //Method: Find the academic year
    //********************************************
    public static String findAcademicYear(Date startDate) {
        //*********************************************************************************
        //if the month of the start date is between september and december the year
        //of the start date is the first element of the academic year, the second element 
        //is the first incremented of 1.
        //
        // if the month is between january and june the first element is year-1 
        //and the second is year
        //*********************************************************************************
        //variable
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        String academicYear = "";

        //recover the month
        int startMonth = c.get(Calendar.MONTH);

        //built the academic year
        if (startMonth >= 8 && startMonth <= 11) {
            academicYear = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.YEAR) + 1);
        } else if (startMonth >= 0 && startMonth <= 5) {
            academicYear = (c.get(Calendar.YEAR) - 1) + "-" + c.get(Calendar.YEAR);
        }

        //return result
        return academicYear;
    }
    
    public static String nextAcademicYear(String currentAcademicYear){
        //1. recover the two number in the string
        String firstYear = currentAcademicYear.substring(0, 4);
        String lastYear = currentAcademicYear.substring(5, 9);
        
        //2. convert to Integer
        Integer first = Integer.parseInt(firstYear);
        Integer last = Integer.parseInt(lastYear);
        
        //3. add one year to each
        first ++;
        last ++;
        
        //4. reconvert to string
        String str = first + "-" + last;
        
        //5. return
        return str;
    }

    
    //*****************************************************
    //Method to find the current Date
    //useful at several place in the program
    //*****************************************************
    public static Date getCurrentDate() throws ParseException {
        Calendar c = Calendar.getInstance();
        Date current = c.getTime();
        c.setTime(current);
        String dateToString = new SimpleDateFormat("yyyy-MM-dd").format(current);
        Date currentDate = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(dateToString).getTime());
        return currentDate;
    }
    
    //************************************************
    //Convert Date to Local Date -> for p:schedule
    //************************************************
    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
    return dateToConvert.toInstant()
      .atZone(ZoneId.systemDefault())
      .toLocalDate();
}
    
    //*************************************************
    //Method to increment the date from x days
    //*************************************************
    public static Date dateDaysIncrement(Date date, int nbDays) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, nbDays);
        date = c.getTime();
        return date;
    }
    
    //**********************************************************************
    //Method to calculate the number of days to add to one date
    //in order to increase the date to the next year and arrive at the same
    //day of the week depending if this is a bisextile year or not
    //**********************************************************************
    public static Integer calculateDaysToAddOneMoreYear(Date startDate) {
            return 364;
    }

}
