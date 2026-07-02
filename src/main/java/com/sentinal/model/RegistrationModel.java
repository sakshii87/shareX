 package com.sentinal.model;

// public class RegistrationModel {

//     private String name;
//     private String email;
//     private String password;

//     // Constructor
//     public RegistrationModel(String name, String email, String password) {
//         this.name = name;
//         this.email = email;
//         this.password = password;
//     }

//     // 🔹 ADD NO-ARG CONSTRUCTOR (Fix for Error 3 below)
//     public RegistrationModel() {
//     }

//     // Getters
//     public String getName() {
//         return name;
//     }

//     public String getEmail() {
//         return email;
//     }

//     public String getPassword() {
//         return password;
//     }

   

//     // Setters
//     public void setName(String name) {
//         this.name = name;
//     }

//     public void setEmail(String email) {
//         this.email = email;
//     }

//     public void setPassword(String password) {
//         this.password = password;
//     }

// }




public class RegistrationModel {
    private String Name;
    private String email;
    private String password;
    private String profileImageUrl;

    // ✅ Add this constructor if it’s missing
    public RegistrationModel(String Name, String email, String password) {
        this.Name = Name;
        this.email = email;
        this.password = password;
    }

    // ✅ You can also add this one if needed elsewhere
    public RegistrationModel() {
    }

    // ✅ Add getters and setters
    public String getName() { return Name; }
    public void setName(String Name) { this.Name = Name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
}
