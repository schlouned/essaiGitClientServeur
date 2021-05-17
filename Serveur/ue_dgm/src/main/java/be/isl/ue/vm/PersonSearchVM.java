/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.isl.ue.vm;

/****************************************
 * Parameters use to search a person
 * every parameter is a String
 * 
 * @author Schloune Denis
 ***************************************/
public class PersonSearchVM {
    //*************************************
    //members
    //*************************************
    private String firstName;
    private String lastName;
    private String city;
    private String dateOfBirth;
    private String email;
    
    //*************************************
    //constructor
    //*************************************
    //default constructor
    public PersonSearchVM(){
    }
    
    //parametrable constructor
    public PersonSearchVM(String firstName, String lastName, String city, String dateOfBirth, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
    }
    
    //********************************************
    //get
    //********************************************
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCity() {
        return city;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }
    
    //********************************************
    //set
    //********************************************
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
}
