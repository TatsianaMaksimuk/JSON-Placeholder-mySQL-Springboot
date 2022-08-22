package com.careerdevs.jsonplaceholder.models;

import com.fasterxml.jackson.annotation.JsonInclude;

//data that won't be specified in a model will be no longer received as a part of response
@JsonInclude(JsonInclude.Include.NON_NULL)
//if the value of fields is null, it will be ignored in final output, if the value is null, field won't be included in response
public class UserModel {

    private int id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;

    //company is a nested object
    private UserCompany company;

    //user company - inner class
    public static class UserCompany {

        private String name;
        private String catchPhrase;
        private String bs;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCatchPhrase() {
            return catchPhrase;
        }

        public void setCatchPhrase(String catchPhrase) {
            this.catchPhrase = catchPhrase;
        }

        public String getBs() {
            return bs;
        }

        public void setBs(String bs) {
            this.bs = bs;
        }

    }


    //did not generate setters for address
    private UserAddress address;

    public static class UserAddress {
        private String suite;
        private String city;
        private String zipcode;



        private AddressGeo geo;

        public static class AddressGeo {

            private String lng;
            private String lat;

            public String getLng() {
                return lng;
            }

            public String getLat() {
                return lat;
            }

        }

        public String getSuite() {
            return suite;
        }

        public String getCity() {
            return city;
        }

        public String getZipcode() {
            return zipcode;
        }

        public AddressGeo getGeo() {
            return geo;
        }


    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public UserCompany getCompany() {
        return company;
    }

    public void setCompany(UserCompany company) {
        this.company = company;
    }


    public UserAddress getAddress() {
        return address;
    }


}
