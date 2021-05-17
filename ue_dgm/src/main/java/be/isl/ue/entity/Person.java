/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Logger;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/***********************************************
 * Class linked to the person table
 * 
 * @author Schloune Denis
 ***********************************************/
@Entity
@Table(name = "person")
@NamedQueries({
    @NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p"),
    @NamedQuery(name = "Person.findByPersonId", query = "SELECT p FROM Person p WHERE p.personId = :personId"),
    @NamedQuery(name = "Person.findByAddress", query = "SELECT p FROM Person p WHERE p.address = :address"),
    @NamedQuery(name = "Person.findByCity", query = "SELECT p FROM Person p WHERE p.city = :city"),
    @NamedQuery(name = "Person.findByCountry", query = "SELECT p FROM Person p WHERE p.country = :country"),
    @NamedQuery(name = "Person.findByDateOfBirth", query = "SELECT p FROM Person p WHERE p.dateOfBirth = :dateOfBirth"),
    @NamedQuery(name = "Person.findByEmail", query = "SELECT p FROM Person p WHERE p.email = :email"),
    @NamedQuery(name = "Person.findByFirstName", query = "SELECT p FROM Person p WHERE p.firstName = :firstName"),
    @NamedQuery(name = "Person.findByIsJuryMember", query = "SELECT p FROM Person p WHERE p.isJuryMember = :isJuryMember"),
    @NamedQuery(name = "Person.findByIsTeacher", query = "SELECT p FROM Person p WHERE p.isTeacher = :isTeacher"),
    @NamedQuery(name = "Person.findByName", query = "SELECT p FROM Person p WHERE p.name = :name"),
    @NamedQuery(name = "Person.findByMobile", query = "SELECT p FROM Person p WHERE p.mobile = :mobile"),
    @NamedQuery(name = "Person.findByPostalCode", query = "SELECT p FROM Person p WHERE p.postalCode = :postalCode")})

public class Person extends AbstractEntity implements Serializable, Comparable<Person> {
    //********************************************
    //members
    //********************************************
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "person_id")
    private Integer personId;
    @Size(max = 255)
    @Column(name = "address")
    private String address;
    @Size(max = 255)
    @Column(name = "city")
    private String city;
    @Size(max = 255)
    @Column(name = "country")
    private String country;
    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "email")
    private String email;
    @Size(max = 255)
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "is_jury_member")
    private Boolean isJuryMember;
    @Column(name = "is_teacher")
    private Boolean isTeacher;
    @Size(max = 255)
    @Column(name = "last_name")
    private String name;
    @Size(max = 255)
    @Column(name = "mobile")
    private String mobile;
    @Size(max = 255)
    @Column(name = "postal_code")
    private String postalCode;
    @ManyToMany(mappedBy = "persons")
    private Collection<OrganizedUe> organizedUes;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "person")
    private Collection<StudentOrganizedUe> studentOrganizedUes;
    @OneToMany(mappedBy = "person")
    private Collection<Section> sections;
    @OneToMany(mappedBy = "person")
    private Collection<Planning> plannings;
    @OneToMany(mappedBy = "person")
    private Collection<Presence> presences;


    //**********************************************
    //constructors
    //**********************************************   
    //default constructor
    public Person() {
    }

    public Person(Integer personId) {
        this.personId = personId;
    }
    
    //parametrable constructor
    public Person(Integer personId, String firstName, String lastName, String mobile, String email, String address, String postalCode, String city, String country, Boolean isTeacher, Date dateOfBirth, Boolean isJuryMember) {
        this.personId = personId;
        this.firstName = firstName;
        this.name = lastName;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.isTeacher = isTeacher;
        this.dateOfBirth = dateOfBirth;
        this.isJuryMember = isJuryMember;
    }
    
    // parametrable constructor with a dateOfBirth parameter in String
     public Person(Integer personId, String firstName, String lastName, String mobile, String email, String address, String postalCode, String city, String country, Boolean isTeacher, String dateOfBirth, Boolean isJuryMember) {
        this.personId = personId;
        this.firstName = firstName;
        this.name = lastName;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.isTeacher = isTeacher;
        this.dateOfBirth = null;
        this.isJuryMember = isJuryMember;
        
        this.setDateOfBirth(dateOfBirth);
    }
     
     //Method from abstract class AbstractEntity
    @Override
    public Integer getId() {
        return personId;
    }
    
    //*****************************
    //getter and setter
    //*****************************
    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    //setDateOfBirth from a string -> conversion
    public void setDateOfBirth(String date){
         try { 
           SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date convertedDate = dateFormat.parse(date);
            this.dateOfBirth = convertedDate;
        } catch (ParseException ex) {
            Logger.getLogger(Person.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
             this.dateOfBirth = null;
        }    
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Boolean getIsJuryMember() {
        return isJuryMember;
    }

    public void setIsJuryMember(Boolean isJuryMember) {
        this.isJuryMember = isJuryMember;
    }

    public Boolean getIsTeacher() {
        return isTeacher;
    }

    public void setIsTeacher(Boolean isTeacher) {
        this.isTeacher = isTeacher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    @JsonbTransient
    public Collection<OrganizedUe> getOrganizedUes() {
        return organizedUes;
    }

    public void setOrganizedUes(Collection<OrganizedUe> organizedUes) {
        this.organizedUes = organizedUes;
    }
    
    @JsonbTransient
    public Collection<StudentOrganizedUe> getStudentOrganizedUes() {
        return studentOrganizedUes;
    }

    public void setStudentOrganizedUes(Collection<StudentOrganizedUe> studentOrganizedUes) {
        this.studentOrganizedUes = studentOrganizedUes;
    }

    @JsonbTransient
    public Collection<Section> getSections() {
        return sections;
    }

    public void setSections(Collection<Section> sections) {
        this.sections = sections;
    }
    
    @JsonbTransient
    public Collection<Planning> getPlannings() {
        return plannings;
    }

    public void setPlannings(Collection<Planning> plannings) {
        this.plannings = plannings;
    }
    
    @JsonbTransient
    public Collection<Presence> getPresences() {
        return presences;
    }

    public void setPresences(Collection<Presence> presences) {
        this.presences = presences;
    }

    //************************************************
    //methods
    //************************************************
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (personId != null ? personId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Person)) {
            return false;
        }
        Person other = (Person) object;
        if ((this.personId == null && other.personId != null) || (this.personId != null && !this.personId.equals(other.personId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name + " " + firstName;
    }
    
    //compareTo: I decided to use firstName, name and dateOfBirth to
    //discriminate a person, I just need to convert a Date in String with the method dateToString below
    public int compareTo(Person p) { 
        Boolean lastNameBool = false;
        Boolean firstNameBool = false;
        Boolean dateOfBirthBool = false;
        String strThis = "";
        String strP = "";
        
        if(this.name != null && this.name.length() > 0 && p.getName() != null && p.getName().length() > 0){
            lastNameBool = true;
            strThis += this.name;
            strP += p.getName();
        }
        if(this.firstName != null && this.firstName.length() > 0 && p.getFirstName() != null && p.getFirstName().length() > 0){
            firstNameBool = true;
            strThis += this.firstName;
            strP += p.getFirstName();
        }
        if(this.dateOfBirth != null && p.getDateOfBirth() != null){
            dateOfBirthBool = true;
            strThis += this.dateOfBirth;
            strP += p.getDateOfBirth();
        }
        if(!(firstNameBool || lastNameBool || dateOfBirthBool)){
//            return this.personId.compareTo(p.personId);
            return 0;
        }
        //Put every string in lower case to have the right sort in the list//       
        strThis = strThis.toLowerCase();
        strP = strP.toLowerCase();
        //*******************************
        
        return strThis.compareTo(strP);
        
    }
    
}
