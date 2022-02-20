package models;

@SuppressWarnings("serial")
public class PhoneException extends Exception {

    public PhoneException(){
        super("Phone number must be 10 digits");
    }

}
