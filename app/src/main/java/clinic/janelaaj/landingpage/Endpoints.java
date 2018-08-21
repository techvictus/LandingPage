package clinic.janelaaj.landingpage;

/**
 *  {@link Endpoints} is used to store all the NodeJs APIs
 *
 *  @author Sambit Mallick (sambit-m)
 *  Created by Sambit Mallick in 20.08.2018
 */

public class Endpoints {

    public static final String BASE_URL = "http://35.200.243.43:3000/";

    /**
     *  Node js APIs to call ENDPOINTS
     *  Method: GET/POST, Query: query (DATA TYPE)
     */

    public static final String GET_LOCALITY = "getlocality"; // Method: POST, Query: cityname (String)
    public static final String GET_DOCTORS_BY_LOCATION = "getdoctorsbylocation"; // Method: POST, Query: cityname (String), localityname (String), localitylat (double), localitylong(double)
}
