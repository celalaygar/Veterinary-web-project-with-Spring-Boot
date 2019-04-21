package com.example.demo.util;

public final class ApiPaths {
	private static final String BASE_PATH = "/api";
	
    public static final class UserBasicCtrl {
        public static final String CTRL = "/user";
    }

    public static final class CustomerBasicCtrl {
        public static final String CTRL = "/customer";
    }
    
    public static final class PetBasicCtrl {
        public static final String CTRL = "/pet";
    }
	
    public static final class UserCtrl {
        public static final String CTRL = BASE_PATH + "/user";
    }

    public static final class CustomerCtrl {
        public static final String CTRL = BASE_PATH + "/customer";
    }
    
    public static final class PetCtrl {
        public static final String CTRL = BASE_PATH + "/pet";
    }
}
