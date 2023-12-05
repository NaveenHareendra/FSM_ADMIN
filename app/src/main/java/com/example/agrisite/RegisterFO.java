package com.example.agrisite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Connection;
import java.sql.Statement;
import io.github.muddz.styleabletoast.StyleableToast;

public class RegisterFO extends AppCompatActivity {

    DatabaseReference obj;
    String selectedProvince, selectedDivision, selectedVSDomain,selectedRole;
    TextInputEditText fullname, username, password, confirmP;
    Spinner province, division, VSDomain;
    RadioButton radio_Admin, radio_FO;
    RadioGroup radioGroupUserRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_fo);

/*        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        obj = FirebaseDatabase.getInstance().getReference().child("Users");

        //DEFINE THE ARRAY OF PROVINCES
        String[] provinceitems = {"Central Province", "North Central Province", "Eastern Province", "Northern Province", "Western Province", "North Western Province", "Sabaragamuwa Province", "Southern Province", "Uva Province"};

        //DEFINE THE ARRAY OF DIVISIONS
        String[] centralDivisions = {"Kandy" ,"Matale", "Nuwara Eliya"};
        String[] easternDivisions = {"Ampara", "Batticaloa", "Trincomale"};
        String[] northernDivisions = {"Jaffna", "Mannar", "Mulativu", "Vavuniya", "Kilinochchi"};
        String[] northwesternDivisions = {"Puttalam", "Kurunegala"};
        String[] sabaragamuwaDivisions = {"Kegalle", "Rathnapura"};
        String[] westernDivisions = {"Colombo", "Gampaha", "Kalutara"};
        String[] southernDivisions = {"Galle", "Matara", "Hambantota"};
        String[] uvaDivisions = {"Badulla", "Monaragala"};

        //DEFINE THE ARRAY OF VSDOMAINS
        String[] KandyDomains = {"Akurana","Delthota","Medadumbara","Pathahewaheta","Pathadumbara","Poojapitiya","Pasbage Korale","Panvila","Doluwa","Kundasale","Minipe","Harispattuwa","Gagawata Korale","Ganga Ihala Korale","Hatharaliyadda","Thumpane","Udadumbara","Udapalatha","Udunuwara","Yatinuwara"};
        String[] MataleDomains = {"Ambanganga Korale","Dambulla","Galewela","Laggala-Pallegama","Matale","Naula","Pallepola","Rattota", "Ukuwela", "Wilgamuwa","Yatawatta"};
        String[] NuwaraEliyaDomains = {"NuwaraEliya","Ambagamuwa","Kotmale","Walapane","Hanguranketha"};

        String[] AmparaDomains = {"Addalachchenai","Akkaraipattu","Alayadiwembu","Ampara","Damana","Dehiattakandiya","Eragama","Kalmunai Muslim", "Kalmunai Tamil", "Karaitivu", "Lahugala", "Mahaoya", "Navithanveli", "Ninthavur", "Padiyathalawa", "Pothuvil", "Sainthamarathu", "Samanthurai", "Thirukkovil", "Uhana"};
        String[] BatticaloaDomains = {"Eravur Pattu","Eravur Town", "Kattankudy", "Koralai Pattu", "Koralai Pattu Central", "Koralai Pattu North", "Koralai Pattu South", "Koralai Pattu West", "Manmunai North", "Manmunai Pattu", "Manmunai S. and Eruvil Pattu", "Manmunai South West", "Manmunai West", "Porativu Pattu"};
        String[] TrincomaleeDomains = {"Gomarankadawala", "Kantalai", "Kinniya", "Kuchchaveli", "Morawewa", "Muttur", "Padavi Sri Pura", "Seruvila", "Thambalagamuwa", "Trincomalee", "Verugal"};

        String[] JaffnaDomains = {"Delft", "Island North", "Island South", "Jaffna", "Karainagar", "Nallur", "Thenmaradchi", "Vadamaradchi East", "Vadamaradchi North", "Vadamaradchi South-West", "Valikamam East", "Valikamam North", "Valikamam South", "Valikamam South-West", "Valikamam West"};
        String[] MannarDomains = {"Madhu", "Mannar", "Manthai West", "Musalai", "Nanaddan"};
        String[] KilinochchiDomains = {"Kandavalai", "Karachchi", "Pachchilaipalli", "Poonakary"};
        String[] MullaitivuDomains = {"Manthai East", "Maritimepattu", "Oddusuddan", "Puthukudiyiruppu", "Thunukkai", "Welioya"};
        String[] VavuniyaDomains = {"Vavuniya", "Vavuniya North", "Vavuniya South", "Vengalacheddikulam"};

        String[] AnuradhapuraDomains = {"Galnewa", "Galenbindunuwewa","Horowpothana","Ipalogama","Kahatagasdigiliya","Kebithigollewa", "Kekirawa", "Mahavilachchiya", "Medawachchiya", "Mihinthale", "Nachchadoowa", "Nochchiyagama", "Nuwaragam Palatha Central", "Nuwaragam Palatha East", "Padaviya", "Palagala", "Palugaswewa", "Rajanganaya", "Rambewa", "Thalawa", "Thambuttegama", "Thirappane",};
        String[] PolonnaruwaDomains = {"Dimbulagala", "Elahera", "Hingurakgoda", "Lankapura", "Medirigiriya", "Thamankaduwa", "Welikanda"};

        String[] KurunegalaDomains = {"Alawwa", "Ambanpola", "Bamunakotuwa", "Bingiriya", "Ehetuwewa", "Galgamuwa", "Ganewatta", "Giribawa", "Ibbagamuwa", "Katupotha", "Kobeigane", "Kotavehera", "Kuliyapitiya East", "Kuliyapitiya West", "Kurunegala", "Mahawa", "Mallawapitiya", "Maspotha", "Mawathagama", "Narammala", "Nikaweratiya", "Panduwasnuwara", "Pannala", "Polgahawela", "Polpithigama", "Rasnayakapura", "Rideegama", "Udubaddawa", "Wariyapola", "Weerambugedara"};
        String[] PuttalamDomains = {"Anamaduwa", "Arachchikattuwa", "Chilaw", "Dankotuwa", "Kalpitiya", "Karuwalagaswewa", "Madampe", "Mahakumbukkadawala", "Mahawewa", "Mundalama", "Nattandiya", "Nawagattegama", "Pallama", "Puttalam", "Vanathavilluwa", "Wennappuwa"};
        String[] KegalleDomains = {"Aranayaka", "Bulathkohupitiya", "Dehiovita", "Deraniyagala", "Galigamuwa", "Kegalle", "Mawanella", "Rambukkana", "Ruwanwella", "Warakapola", "Yatiyanthota"};
        String[] RatnapuraDomains = {"Ayagama", "Balangoda", "Eheliyagoda", "Elapattha", "Embilipitiya", "Godakawela", "Imbulpe", "Kahawatta", "Kalawana", "Kiriella", "Kolonna", "Kuruvita", "Nivithigala", "Opanayaka", "Pelmadulla", "Ratnapura", "Weligepola"};

        String[] GalleDomains = {"Akmeemana", "Ambalangoda", "Baddegama", "Balapitiya", "Benthota", "Bope-Poddala", "Elpitiya", "Galle", "Gonapinuwala", "Habaraduwa", "Hikkaduwa", "Imaduwa", "Karandeniya", "Nagoda", "Neluwa", "Niyagama", "Thawalama", "Welivitiya-Divithura", "Yakkalamulla"};
        String[] HambantotaDomains = {"Ambalantota", "Angunakolapelessa", "Beliatta", "Hambantota", "Katuwana", "Lunugamvehera", "Okewela", "Sooriyawewa", "Tangalle", "Thissamaharama", "Walasmulla", "Weeraketiya"};
        String[] MataraDomains = {"Akuressa", "Athuraliya", "Devinuwara", "Dickwella", "Hakmana", "Kamburupitiya", "Kirinda Puhulwella", "Kotapola", "Malimbada", "Matara", "Mulatiyana", "Pasgoda", "Pitabeddara", "Thihagoda", "Weligama", "Welipitiya"};

        String[] BadullaDomains = {"Badulla", "Bandarawela", "Ella", "Haldummulla", "Hali-Ela", "Haputale", "Kandaketiya", "Lunugala", "Mahiyanganaya", "Meegahakivula", "Passara", "Rideemaliyadda", "Soranathota", "Uva-Paranagama", "Welimada"};
        String[] MoneragalaDomains = {"Badalkumbura", "Bibile", "Buttala", "Katharagama", "Madulla", "Medagama", "Moneragala", "Sevanagala", "Siyambalanduwa", "Thanamalvila", "Wellawaya"};

        String[] ColomboDomains = {"Colombo", "Ratmalana", "Padukka ","Dehiwala", "Homagama ","Seethawaka","Kaduwela","Kesbewa","Thimbirigasyaya","Kolonnawa","Kotte", "Maharagama","Moratuwa"};
        String[] GampahaDomains = {"Attanagalla", "Biyagama", "Divulapitiya", "Dompe", "Gampaha", "Ja-Ela", "Katana", "Kelaniya", "Mahara", "Minuwangoda", "Mirigama", "Negombo", "Wattala"};
        String[] KalutaraDomains = {"Agalawatta", "Bandaragama", "Beruwala", "Bulathsinhala", "Dodangoda", "Horana", "Ingiriya", "Kalutara", "Madurawela", "Mathugama", "Millaniya", "Palindanuwara", "Panadura", "Walallavita"};

        //REFERENCES FOR EDIT TEXTS

        fullname = findViewById(R.id.editTxtFullName);
        username = findViewById(R.id.editTxtUsername);
        password = findViewById(R.id.editTxtPassword);
        confirmP = findViewById(R.id.editTxtCnfPassword);

        //REFERENCE FOR Radio Buttons
        radioGroupUserRole = findViewById(R.id.radioGroupUserRole);
        radio_Admin = findViewById(R.id.radioAdmin);
        radio_FO = findViewById(R.id.radioFO);

        //REFERENCE FOR BUTTON
        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnClear = findViewById(R.id.btnClear);

        //REFERENCES FOR ALL THREE ARRAYS
        province = findViewById(R.id.ProvincePicker);
        division = findViewById(R.id.DivisionPicker);
        VSDomain = findViewById(R.id.VillageServiceDomainPicker);

        // Create the ArrayAdapter for the province Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, provinceitems);
        province.setAdapter(adapter);

        // Create an empty ArrayAdapter for the division Spinner
        ArrayAdapter<String> divisionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        division.setAdapter(divisionAdapter);

        // Create an empty ArrayAdapter for the VSDomain Spinner
        ArrayAdapter<String> VSDomainAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        VSDomainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        VSDomain.setAdapter(VSDomainAdapter);

        // Set the OnItemSelectedListener for the province Spinner
        province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Get the selected item from the picker and assign it to selectedProvince variable
                selectedProvince = parent.getItemAtPosition(position).toString();

                // Update the division Picker based on the selected province
                if (selectedProvince.equals("Central Province")) {

                    // Update the divisionAdapter with centralDivisions array
                    divisionAdapter.clear();
                    divisionAdapter.addAll(centralDivisions);

                }else if(selectedProvince.equals("Eastern Province")){

                    //Update the divisionAdapter with easternDivisions array

                    divisionAdapter.clear();
                    divisionAdapter.addAll(easternDivisions);

                }else if(selectedProvince.equals("Northern Province")){

                    // Update the divisionAdapter with northernDivisions array

                    divisionAdapter.clear();
                    divisionAdapter.addAll(northernDivisions);

                }else if(selectedProvince.equals("North Western Province")){

                    // Update the divisionAdapter with northwesternDivisions array

                    divisionAdapter.clear();
                    divisionAdapter.addAll(northwesternDivisions);

                }else if(selectedProvince.equals("Sabaragamuwa Province")){

                    // Update the divisionAdapter with sabaragamuwaDivisions array

                    divisionAdapter.clear();
                    divisionAdapter.addAll(sabaragamuwaDivisions);

                } else if (selectedProvince.equals("Western Province")) {

                    // Update the divisionAdapter with westernDivisions array

                    divisionAdapter.clear();
                    divisionAdapter.addAll(westernDivisions);

                } else if (selectedProvince.equals("Southern Province")) {

                    // Update the divisionAdapter with southernDivisions array

                    divisionAdapter.clear();
                    divisionAdapter.addAll(southernDivisions);
                }

                else if(selectedProvince.equals("Uva Province")){
                    // Update the divisionAdapter with uvaDivisions array
                    divisionAdapter.clear();
                    divisionAdapter.addAll(uvaDivisions);
                }

                // Notify the divisionAdapter that the data has changed
                divisionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Set the OnItemSelectedListener for the VSDomain Spinner
        division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //Get the selected item from the picker and assign it to selectedProvince variable
                selectedDivision = parent.getItemAtPosition(position).toString();

                // Update the division Picker based on the selected province
                if (selectedDivision.equals("Kandy")) {

                    // Update the VSDomainAdapter with KandyDomains Array

                    VSDomainAdapter.clear();
                    VSDomainAdapter.addAll(KandyDomains);

                } else if (selectedDivision.equals("Matale")) {

                    //Update the divisionAdapter with easternDivisions array

                    VSDomainAdapter.clear();
                    VSDomainAdapter.addAll(MataleDomains);

                } else if (selectedDivision.equals("Nuwara Eliya")) {

                    // Update the divisionAdapter with NuwaraEliyaDomains array

                    VSDomainAdapter.clear();
                    VSDomainAdapter.addAll(NuwaraEliyaDomains);

                } else if (selectedDivision.equals("Ampara")) {

                    // Update the divisionAdapter with AmparaDivisions array

                    VSDomainAdapter.clear();
                    VSDomainAdapter.addAll(AmparaDomains);

                } else if (selectedDivision.equals("Batticaloa")) {

                    // Update the divisionAdapter with BatticaloaDomains array

                    VSDomainAdapter.clear();
                    VSDomainAdapter.addAll(BatticaloaDomains);

                } else if (selectedDivision.equals("Trincomalee")) {

                    // Update the divisionAdapter with TrincomaleeDivisions array

                    VSDomainAdapter.clear();
                    VSDomainAdapter.addAll(TrincomaleeDomains);

                } else if (selectedDivision.equals("Anuradhapura")) {

                    // Update the divisionAdapter with southernDivisions array

                    VSDomainAdapter.clear();
                    VSDomainAdapter.addAll(AnuradhapuraDomains);

                } else if (selectedDivision.equals("Polonnaruwa")) {

                    // Update the divisionAdapter with uvaDivisions array

                    VSDomainAdapter.clear();
                    VSDomainAdapter.addAll(PolonnaruwaDomains);

                } else if (selectedDivision.equals("Jafna")) {

                    // Update the VSDomainAdapter with JaffnaDomains array

                    VSDomainAdapter.clear();
                    VSDomainAdapter.addAll(JaffnaDomains);

                } else if (selectedDivision.equals("Kilinochchi")) {

                    // Update the VSDomainAdapter with JaffnaDomains array

                    VSDomainAdapter.clear();
                    VSDomainAdapter.addAll(KilinochchiDomains);

                }   else if (selectedDivision.equals("Mannar")) {

                    // Update the VSDomainAdapter with JaffnaDomains array

                    VSDomainAdapter.clear();
                    VSDomainAdapter.addAll(MannarDomains);

                } else if (selectedDivision.equals("Mullaitivu")) {
                    // Update the VSDomainAdapter with JaffnaDomains array
                    VSDomainAdapter.clear();
                    VSDomainAdapter.addAll(MullaitivuDomains);

                } else if (selectedDivision.equals("Vavuniya")) {

                    // Update the VSDomainAdapter with VavuniyaDomains array

                    VSDomainAdapter.clear();
                    VSDomainAdapter.addAll(VavuniyaDomains);

                }else if (selectedDivision.equals("Kurunegala")) {

                    // Update the VSDomainAdapter with KurunegalaDomains array

                    VSDomainAdapter.clear();
                    VSDomainAdapter.addAll(KurunegalaDomains);

                }else if (selectedDivision.equals("Puttalam")) {

                    // Update the VSDomainAdapter with PuttalamDomains array

                    VSDomainAdapter.clear();
                    VSDomainAdapter.addAll(PuttalamDomains);

                }else if (selectedDivision.equals("Kegalle")) {

                    // Update the VSDomainAdapter with RatnapuraDomains array

                    VSDomainAdapter.clear();
                    VSDomainAdapter.addAll(KegalleDomains);

                }else if (selectedDivision.equals("Ratnapura")) {

                    // Update the VSDomainAdapter with RatnapuraDomains array

                    VSDomainAdapter.clear();
                    VSDomainAdapter.addAll(RatnapuraDomains);

                }else if (selectedDivision.equals("Galle")){

                    // Update the VSDomainAdapter with GalleDomains array

                    VSDomainAdapter.clear();
                    VSDomainAdapter.addAll(GalleDomains);

                } else if (selectedProvince.equals("Hambantota")) {

                    // Update the VSDomainAdapter with HambantotaDomains array

                    VSDomainAdapter.clear();
                    VSDomainAdapter.addAll(HambantotaDomains);

                } else if (selectedDivision.equals("Matara")) {

                    // Update the VSDomainAdapter with MataraDomains array

                    VSDomainAdapter.clear();
                    VSDomainAdapter.addAll(MataraDomains);


                } else if (selectedDivision.equals("Badulla")) {

                    // Update the VSDomainAdapter with BadullaDomains array

                    VSDomainAdapter.clear();
                    VSDomainAdapter.addAll(BadullaDomains);

                }else if (selectedDivision.equals("Moneragala")) {

                    // Update the VSDomainAdapter with MoneragalaDomains array

                    VSDomainAdapter.clear();
                    VSDomainAdapter.addAll(MoneragalaDomains);

                }else if (selectedDivision.equals("Colombo")) {

                    // Update the VSDomainAdapter with ColomboDomains array

                    VSDomainAdapter.clear();
                    VSDomainAdapter.addAll(ColomboDomains);

                }else if (selectedDivision.equals("Gampaha")) {

                    // Update the VSDomainAdapter with GampahaDomains array

                    VSDomainAdapter.clear();
                    VSDomainAdapter.addAll(GampahaDomains);

                }else if (selectedDivision.equals("Kalutara")) {

                    // Update the VSDomainAdapter with KalutaraDomains array

                    VSDomainAdapter.clear();
                    VSDomainAdapter.addAll(KalutaraDomains);

                }
                // Notify the divisionAdapter that the data has changed
                VSDomainAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                /*if (validateFullName(fullname.getText().toString()) &&
                        validateUsername(username.getText().toString()) &&
                        validatePassword(password.getText().toString()) &&
                        validateCnfPassword(confirmP.getText().toString()) &&
                        CheckIfSamePassword(password.getText().toString(), confirmP.getText().toString())) {
*/
                    // Validation successful, proceed to insert user data
                    InsertUserData();
                }
            //}
        });

        btnClear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                fullname.setText("");
                username.setText("");
                password.setText("");
                confirmP.setText("");
                radioGroupUserRole.clearCheck();
                province.setSelection(0);
                division.setSelection(0);
                VSDomain.setSelection(0);
            }
        });
    }

    public void InsertUserData() {
        // Retrieve the input values

        String full_name_of_user = fullname.getText().toString();
        String user_name_of_user = username.getText().toString();
        String user_password = password.getText().toString();
        String user_confirmP = confirmP.getText().toString();

        // Retrieve the checked values from the radio buttons
        selectedRole = radio_Admin.isChecked() ? "Admin" : "Field Officer";

        // Retrieve the selected province from the Spinner
        String selectedProvince = province.getSelectedItem().toString();

        //  Retrieve the selected division from the Spinner
        String selectedDivision = division.getSelectedItem().toString();

        // Retrieve the selected VSDomain from the Spinner
        String selectedVSDomain = VSDomain.getSelectedItem().toString();

        // Create a RegisteredUsers object with the retrieved values
        RegisteredUsers users = new RegisteredUsers(full_name_of_user, user_name_of_user,user_password, selectedRole, user_confirmP, selectedProvince, selectedDivision, selectedVSDomain);

        obj.push().setValue(users);

        StyleableToast.makeText(this, "User Registered!", Toast.LENGTH_LONG, R.style.InsertToast).show();
    }

    //connection = databaseHelper.connectionclass();

        /*try {
           if (connection != null) {
                Log.e("Error", "Con is null");

                // Construct the SQL INSERT statement with parameterized query
                //String SQLINSERT = "INSERT INTO USERINFO (UserID, FullName, Username, Password, Province, Divisional_Secretariat) VALUES ('1' ,'jOHN', 'Christ' , '1234ABC', 'Western Province', 'Colombo')";
                String SQLINSERT = "INSERT INTO USERINFO (FullName, Username, Password, Province, Divisional_Secretariat) VALUES ('" + fullName + "', '" + username+ "','" + password + "', '" + province+ "', '" + division+ "')";

                Statement statement = connection.createStatement();
                statement.executeUpdate(SQLINSERT);
                StyleableToast.makeText(this, "Successfully Registered the Field Officer!", Toast.LENGTH_LONG, R.style.InsertToast).show();
                // Additional code if needed after successful insertion
            }
        } catch (Exception exception) {
            Log.e("Error", exception.getMessage());
        }*/

    /*private boolean validateUsername(String username) {
        String pattern = "^[a-zA-Z0-9._-]{4,20}$";

        if (TextUtils.isEmpty(username)) {
            TextInputEditText editText = findViewById(R.id.editTxtUsername);
            editText.setError("Username is required.");
            return false;

        } else if (!username.matches(pattern)) {
            TextInputEditText editText = findViewById(R.id.editTxtUsername);
            editText.setError("Invalid username format. Only letters, numbers, '.', '_', and '-' are allowed.");
            return false;

        } else {
            return true;
        }
    }


    private boolean validateFullName(String fullName){
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if(TextUtils.isEmpty(fullName)){
            TextInputEditText editText = findViewById(R.id.editTxtFullName);
            editText.setError("Full Name is required.");
            return false;

        } else if ( fullName.length() >= 15) {
            TextInputEditText editText = findViewById(R.id.editTxtFullName);
            editText.setError("Full Name can't be too long.");
            return false;
        } else {
            return true;
        }
    }

    private boolean validatePassword(String password) {
        String passwordVal = "^" +
                 "(?=.*[0-9])" +         //at least 1 digit
                 //"(?=.*[a-z])" +         //at least 1 lower case letter
                 //"=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" ;              //at least 4 characters
                 //"$";

        if (TextUtils.isEmpty(password)) {
            TextInputEditText editText = findViewById(R.id.editTxtPassword);
            editText.setError("Password is required.");
            return false;

        } else if (!password.matches(passwordVal)) {
            TextInputEditText editText = findViewById(R.id.editTxtPassword);
            editText.setError("Invalid Password Format.");
            return false;

        } else {
            return true;
        }
    }
    private boolean validateCnfPassword(String confirmP){

        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if(TextUtils.isEmpty(confirmP)){
            TextInputEditText editText = findViewById(R.id.editTxtCnfPassword);
            editText.setError("This field can't be empty.");
            return false;

        } else if (!confirmP.matches(passwordVal)) {
            TextInputEditText editText = findViewById(R.id.editTxtCnfPassword);
            editText.setError("Invalid Password Format..");
            return false;

        } else {
            return true;
        }
    }

    private boolean CheckIfSamePassword(String password, String confirmP){
        if(password!=confirmP){
            TextInputEditText editText = findViewById(R.id.editTxtCnfPassword);
            editText.setError("Both passwords should matches");
            return false;
        }
        return true;
    }*/
}


