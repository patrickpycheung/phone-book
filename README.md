# The Phone Book Application

  Table of contents

  * [**About the application**](#about-the-application)
  * [**Assumptions**](#assumptions)
  * [**Instructions**](#instructions)
    + [**Getting the source and compile the application**](#getting-the-source-and-compile-the-application)
    + [**Running the application**](#running-the-application)
      - [**Console application**](#console-application)
        * [**By docker image (recommended)**](#by-docker-image-recommended)
        * [**By maven**](#by-maven)
      - [**RESTful API endpoints**](#restful-api-endpoints)
  * [**Operation manual**](#operation-manual)
  * [**Architecture**](#architecture)
  * [**Design and development approach**](#design-and-development-approach)
  * [**Technology stack**](#technology-stack)

## **About the application**
  The Phone Book Application is a console application which keeps track of customer contacts. It is written in Java and Spring Boot.
  <img src="https://ypawtw.bn.files.1drv.com/y4mHMJLuFdupmUJA15sKwFPUdp0G2UhitNSCdFMcw0dCN6I_ERG1Y0onKc6aiBEFjd0QBM2c2_KQ4Ex2Z748CEVpQ7s3jrn7xGNIFBGmp1PL5p6msq0YnwH0cK_We3JV8EVpKxTewNseXJNk8tLHCj5dL9rGJAZn9AArUjabyx_JgZAlFojr2TENAy4HP9Cuu-nsfQQxpxxOjxEe4ZnKFJfTQ?width=1544&height=1606&cropmode=none" title="Main_Menu" alt="Main_Menu">
  It has the following functions:
  * Create a new contact entry to an existing phone book
  * Update an existing contact in an existing phone book
  * Read all contacts from all phone books
  * Read all contacts in a phone
  * Retrieve a unique set of all contacts across all phone books
  * Delete an existing contact entry from an existing phone book
## **Assumptions**
The following assumptions are being made for the application:
* The application is a console application.
* RESTful API endpoints are not required, but they are nevertheless prepared for completeness. All the changes made by the console application would be reflected in the API calls, and vice versa.
*	Data is persisted in memory in the form of Spring beans.
* There are 2 phone books, phoneBookA and phoneBookB. 
	* Each phone book contains a list of customers and their phone numbers.
	* One of it is  for walk-in customers, another one is for online customers.
* There will be chances that some customers exist in both phonebooks, as they may have used both means of purchase.
* No phone book will have duplicate entry of a customer.
* The customer name is limited to 70 characters, alpha-numeric, space allowed.
* Customer names are case-sensitive
* Customer number is limited to 30 characters numeric, no space allowed.
* If user tries to create a new entry in a phone book where there is already an existing one, an update operation will be carried out instead.
* There are already some customers existing in the phone books.
* For the sake of synchronicity, if user attempts to update a customer entry in a phone book, where the same entry exist in the other one as well, the same update will be performed in both phone books.
## **Instructions**
  ### **Getting the source and compile the application**
  Clone the "prod" branch of the phone-book repository from GitHub
  
  Git repository location:<br/>
  https://github.com/patrickpycheung/phone-book/tree/prod

  Command: 
  
    git clone https://github.com/patrickpycheung/phone-book

  To compile the application, run the following command at the root folder of the cloned source (i.e. the folder containing the src folder) (Note: Maven required):

    mvn clean package

  <img src="https://lxxtfa.bn.files.1drv.com/y4mjahS2TFQ6bERG4gBXr_IhUHfHx_xNt9WmhQpvOB_m9RIZcC3QyNcG6bciTme1RBZ9mvpvGYwvjLaegQt6fpyLPxYHrCct7TxJ5JFeYikHceD_rAv5BBfzIdyNgkhqM_6fX0Um11BLD3gwZwaOYCfuM2s1-2UhIuIQi7fSeQsBS18h34xeXChNljWi3qDToxQmcMqH2xdaNb_KnU9FzVZ3A?width=1792&height=324&cropmode=none" title="Build_JAR_01" alt="Build_JAR_01">

  <img src="https://knun5w.bn.files.1drv.com/y4mmhJNhg-zNxZEXMUKO5VGBvSoCiNKqhOxFZY4bjO1WHstv9Ufe9IeFJc4AQAMDzHywuTAmU42r3x7z2TotmvPWiM5_AjTMfP8x-XiT-3Cl4EiapfVqOI60wH9xH1pzePRdiI_OJ--huIqo66ysX5IUPdYrh05hv5dTOAmfeelJ8PXjyrjQyL8y9jIASHq1DluP040EAi9pE0Ezr5RpfOiyQ?width=2746&height=924&cropmode=none" title="Build_JAR_02" alt="Build_JAR_02">

  The outcome would be a jar file in the target folder which can be executed as per description in [**here**](#by-maven).

  <img src="https://jwcara.bn.files.1drv.com/y4mzatP6hY5lmQg7UMEGPq3_aafRwW8IJ-Bwz-KJuGRF-RPnhp0t_uC-ykKiM5xKkOBgmpwcURPfHIOuV5nLZ94VHctf7D_YZptiGkKhtuypvkLtDfAFUO5sYwgzzk641eQfc1wMtbyFxL53GHkbRTDKhymrV9QCoQHMPZjbLjM0x-YxPyA3OIwcU5bsIiW13paUzPQTJ9pSpQBmh4xn0AVGg?width=858&height=518&cropmode=none" title="Built_JAR" alt="Built_JAR">

  ### **Running the application**
  #### **Console application**
  ##### **By docker image (recommended)**
  
  Dockerhub repository location:<br/>
  https://hub.docker.com/r/patrick888/phone-book

  Pull the lastest "prod" docker image (docker tag "prod-{version}") from dockerhub with the following command (Note: docker runtime required):

    docker pull patrick888/phone-book:prod-1.0.0

  Run the docker image in interative mode within in a container using the following command:
    
    docker run -it [host port]:[container port] -t patrick888/phone-book:prod-1.0.0

  e.g.
    
    docker run -it -p 8080:8080 -t patrick888/phone-book:prod-1.0.0

  This will start up the application in a container.

  or,
  ##### **By maven**
  1. Go to the root folder of the cloned source.
  1. Either
  * Run as Spring Boot application directly:

    Command:

    ```
    mvn spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=prod
    ```
    
    This will start up the application without building the jar file.
    
    or,

  * Compile the jar with maven as described [**here**](#getting-the-source-and-compile-the-application), and run the jar

    Command:
    ```
    java -Dspring.profiles.active=prod -jar target/phoneBook-1.0.0.jar
    ```
  ####  **RESTful API endpoints**
  The console application makes use of the phoneBookService logics directly. Nevertheless, the following API endpoints are still made available for use to prepare for other integration purpose in the future.

  Verb | Path | Description
  -- | --| --
  POST | /api/phonebook | Create a new contact entry to an existing phone book
  PUT | /api/phonebook/{phoneBookName}/{custName} | Update an existing phone book entry
  GET | /api/phonebook | Read all entries from all phone books
  GET | /api/phonebook/{phoneBookName} | Read all entries from single phone book
  GET | /api/phonebook/unique | Read unique entries from all phone books
  DELETE | /api/phonebook/{phoneBookName}/{custName} | Delete an entry from single phone book

  The required parameters and returns are described in the Swagger UI page.

  <img src="https://lxxvfa.bn.files.1drv.com/y4mWsj1evvC-lkB2t6PodUJmlZcv1YPR39yrf1wVJWNTD20Cy6nwdVfL0DfIm7EeGSwnHNLlCMUsr5QDUf3SQfq7U-gpGFfOCJqFm2zgi0KL6KNC5C3pK8Fn50O_VKjg3rUvqQSiekOcpyzMF2CD-52tvrjO7excrVqYua3I0ueb3T5LYkJKa1WOxvMN43u0pijki4z501KXFYSYjZ6bO887g?width=3348&height=1882&cropmode=none" title="Swagger_UI" alt="Swagger_UI">

  The web server will be started as soon as the application is started.

  Swagger UI path (assuming you are running in local):<br/>
  http://localhost:8080/swagger-ui.html#/phone-book-controller

  Sample call:

  <img src="https://knup5w.bn.files.1drv.com/y4mdFIjqYlabP-KIb-q4ATDyDjHwArlBWPepVskHMsijeiCGY4nNz43YTM_iGpjpK70NjXZqV97kXblQbSZQDZVQhsoK3uJZN2rP7GifZcoKypMAvqqZheAEQxRorLSjEOlRIGdTR-ahuQAIOxDs4t0Xc25dpsv7gKF3QEs46NQrEmogzad2OQEJmpWYPlIYiwTaAPBG_WLgOwPPbtg3pvIPw?width=3348&height=1882&cropmode=none" title="Sample_Call" alt="Sample_Call">
  

## **Operation manual**
After the application starts up, user will see the following menu:

  <img src="https://ypawtw.bn.files.1drv.com/y4mHMJLuFdupmUJA15sKwFPUdp0G2UhitNSCdFMcw0dCN6I_ERG1Y0onKc6aiBEFjd0QBM2c2_KQ4Ex2Z748CEVpQ7s3jrn7xGNIFBGmp1PL5p6msq0YnwH0cK_We3JV8EVpKxTewNseXJNk8tLHCj5dL9rGJAZn9AArUjabyx_JgZAlFojr2TENAy4HP9Cuu-nsfQQxpxxOjxEe4ZnKFJfTQ?width=1544&height=1606&cropmode=none" title="Main_Menu" alt="Main_Menu">

One will then need to select the operation he/she would like to perform.

### Enter "c or C" -> Create a new contact or update an existing contact
The screen will ask the phone book name in which the user would like to create/update the entry, the customer name and the customer phone number.

Note that customer names are case-sensitive.

If the customer is not found in the specified phone book, a new entry will be created in it;

  <img src="https://jwccra.bn.files.1drv.com/y4mxoYsEyOgbSsjCKlow6AqTYbNHHw5JzNdFTZKB86A1P0hm9i-hc9LjzowpXBKvqWSyD9zwRxArSWEXZfSB5fJw2NIfRGZa8vOatqwncrlMUYY2bAoHWFhXyFMupiJtnoxheTcpUnh-Pqtsfx4dzBuWjXmWyi07bKX6g5cwr7-fQ36R3cOeRSMy4pMORQe9E1dNiO0W9FY_W8i-tVjLcwULA?width=1666&height=1230&cropmode=none" title="Create_New" alt="Create_New">

If the customer is found, the existing entry will be updated with the phone number.

In addition, if the customer is also found in the other phone book, the entry will be updated in the other phone book as well.

  <img src="https://zqw9wq.bn.files.1drv.com/y4mYB1X-uy3qaYDIE2o4yzGVt3T6UL4KS8UeNkjVNPsFXMDcg0mNAfybIrlEQt6fiSCR7wcXzhwTNNaAqsSzFtyeH_rY4LvlP2992RBZzftRkLCXsU5jy0zqKYo5mcVGq4VLZXgNTZpNzvHD_ZTR-MLWGEbc7OJPTnekJmhXmzRahII1zQOqZRceNrj-ei4wnY2XVSb4T-DXs239quHZShz4A?width=1666&height=1230&cropmode=none" title="Update_Existing" alt="Update_Existing">

### Enter "a or A" -> Read all contacts from all phone books
The screen will show the latest image of the phone books, showing their entries inside respectively.

  <img src="https://nmozqq.bn.files.1drv.com/y4mhtm525LtWJ2mV2Y08mEWShKY59UcL_3gB8Qfynl56TjkoL2FBxTSRLNyOMZpeYecnP0hGj_OsfOATesUnBDa1lH6ccRZ9RW7dZbxv_Fk5NFBZdRi7BBtVgH4JWa4ivhC36tcUBW16wZGEvB75EUutAy8BfR0JRh7hZBJFk-pnHwHAgWh0K3tRGKmd3MPKTjPvF-KBuN2bab5vQEuL5B3SQ?width=1382&height=796&cropmode=none" title="Read_All_From_All" alt="Read_All_From_All">

### Enter "r or R" -> Read all contacts in a phone book
The screen will ask the phone book name in which the user would like to query, and then show the entries within the specified phone book.

  <img src="https://mp4lnw.bn.files.1drv.com/y4mBoOsendLq_IeTn3IFQo4ctHJXRiA1aTPX__tfhrA4wdE0qI5mrEOqzMaFn6fwMwb4CD_sDnrAStCy3fOfwajTtzIP7eSLavJFk0b4bMIVS8SMEtshrPHhPnkeu2K0PmowPCYPxT3u9ezG8oOKs2W0iG8XOW5vIApNDq3K3rKTmd8woX9gipEYpsy3zFBKoFE4Gh8kj1Jsv4SZHO1DoALaA?width=1382&height=662&cropmode=none" title="Read_All_From_Single" alt="Read_All_From_Single">

### Enter "u or U" -> Retrieve a unique set of all contacts across all phone books
The screen will show a unique set of images across all phone books.

  <img src="https://ptifzw.bn.files.1drv.com/y4m27gs4EC13CbpNxNy6x0tTeUG-2ob90EdIYtZ3jnFxnb5Yzhi5jUqPijcUOCSlBDf982XEDKooPwA6oxeddVSC3KDvo-7i3fOlkZH6ScRHpb5LuHjIVVRHr4-qc1bi2HQsycqcT_JL1L6GBS3V0BeV5Rg2oQs-aZ-6GulcGSxvvCE46duI1P5OyTWYdrdzAWuqdLuC-vug86uNoyVH0JU8Q?width=1510&height=538&cropmode=none" title="Read_Unique_From_All" alt="Read_Unique_From_All">


### Enter "d or D" -> Delete an existing contact entry from an existing phone book
The screen will ask the phone book name in which the user would like to delete the entry from, the customer name and the customer phone number.

If the customer is found, the entry will be deleted from the specified phone book;

  <img src="https://ypavtw.bn.files.1drv.com/y4mJKl9UWnsGY68A-OSyaSxK-atmSp6oc25ll1XISgavbEwqawByUKIinACwSk3XKJPyBpSq2wnctYLVkRR6K-5YCKFSB2eFm3fspGf2FytWrGkT6Uk6p6a7RZZwxsmjxB_dkKp6QhEeswBLnxL10xZa0s7uPcgXymfgV0E3MhYU4V2NCuAXcKr5JQKi7l9hN3uT0RO5HbPdUHdCd6YvCPEqQ?width=1826&height=1058&cropmode=none" title="Delete_Existing" alt="Delete_Existing">

If the customer is not found, nothing will be deleted from it.

  <img src="https://ptiezw.bn.files.1drv.com/y4mvtQqNgtqDt1i055ljuJQ1wLZSNGNCvwVZPSBZNNlwk5rJL2S_nYolXBtDbiBJHrJdWs9uFs2RHjT5lnupSmS4hO2Mp9Tt6YAViTTfo54466z0S4KdM4diCCFEFsjVT_iMqUsSVlBR6t8OHVdAu9WUOYB4_e1_JXdy_J_6Q9H6PQd0Tzun53Vk4Qkn6zEIFLC4LOv6AxlweVmGDNg5WXXYQ?width=1846&height=392&cropmode=none" title="No_Action_On_Delete_Non_Existing" alt="No_Action_On_Delete_Non_Existing">


Note that entries in the other phone book will not be affected, even if it contains the concerned entry.

  <img src="https://knuo5w.bn.files.1drv.com/y4mtnH6cAiAMPuD-OqkmxDg-rmbwN0R0wgpFwCmLedH_DiA4heWCdvLqOeIDpRweP5dlkUf4IrV-50TaMjKlKkvuNAbkn9kgy_LJMMb5eIj1Xm0cx3LlTojOzZT58CWGwcsl_KbQDq7Yf3laKHlqluaXwRrJ4f0yvJI1x7tO72jmeE1ZG2wcuckuYebxMT57k423lwsTgY7-Q_IIv-MInn_bA?width=1820&height=1038&cropmode=none" title="Delete_Existing_And_The_Other_Phone_Book_Has_Same_Entry" alt="Delete_Existing_And_The_Other_Phone_Book_Has_Same_Entry">

### Enter "q or Q" -> Quit application
The application will terminate.

  <img src="https://jwcbra.bn.files.1drv.com/y4mSjSq29SxHD1d5-fq1i3aRSA1Fxx2H36Q3RDM-5znY2SEJs25_40TMQRfLtjepuJgjBtXuttcNEmPwC_j8fy6Z8JvN14fAuRIJx4d7Z1jcA_DAm_QjAmVQwOlpB6HoXk6_8zCBUygSxwLg9YbJYAYaRiysrmYbfZCOu1M7P5JbFqHhBXk11bpUvBRoQlgUqz2V5TLWcgvDW48LJ5ms3ED0Q?width=202&height=146&cropmode=none" title="Quit" alt="Quit">

### Invalid input

If the user provided an input which is invalid, the screen will show the issue to the user on screen, and then return to the main menu.

Note the constraints on the inputs as below:

  Item | Constraint | Regex
  -- | --| --
  Operation | As per screen instruction, case-insensitive | "^[cCaArRuUdDqQ]$"
  Phone book name | "A" or "B", case-insensitive  | "^[aAbB]$"
  Customer name | 70 characters, alpha-numeric, space allowed, case sensitive | "^[a-zA-Z0-9 ]{1,70}$"
  Customer phone number | 30 characters, numeric, no space allowed | "^[0-9]{1,30}$"

* Invalid operation

  <img src="https://ahpjqa.bn.files.1drv.com/y4mDm3FmRwvHX4AaGehhowaAThiUMKRsXsisYE7eS_3p41K9LPnSKdqDRiZQ7oombRgXLwh5-RXvqsLGrEWTleBa9N_E888Ib_cTRc3N-MMthtIaruVdUNp3YleOrvK3Yen-CT-xJOalcZb1COQrzOegYSNYJGygxmOWUCkYqCKEdtVuo28Y2Ykn7tXIBGEoPeWaxcq5hIQ91Xc9aBYgvodsA?width=1512&height=826&cropmode=none" title="Invalid_Operation" alt="Invalid_Operation">

* Invalid phone book name 

  <img src="https://zqw8wq.bn.files.1drv.com/y4mBwCdqGAa3KzE0sI9rJyz1zqanvkwUUuCpzBrnEEjtYR9vDqD7vIF7BM9ANqy4giu9vCqmCDql5--pkOPLXAEQs-14iKMbnshLkiXtcc8wsZezZbG0Aa1g5g_v1XHutv6szQz7dXorl6NZ1rcNuarfqDHZYH2Z7W_lp2WOOsN8lEsfklycHX5FGL1lSjNqBonB76606qP3WH7liTpASR7sw?width=1518&height=758&cropmode=none" title="Invalid_Phone_Book_Name" alt="Invalid_Phone_Book_Name">

* Invalid customer name

  <img src="https://nmoyqq.bn.files.1drv.com/y4mcODbhy2dwBhNts4DYiC0dHQbmuRSdsGVcfwUuMC69-XWoaAdZ5CCO1RBp4xBMNMDjGRiT6KhjMF1ASRFwVTj4ThOCZJXrKwXeldfy70V6sgAyFrww1BBFLLNibZdah03autZMBTwREN-NVEz3ivnK1KdGm8Ktbx6UxqBUKp1rmut4ZxsPoGSW19YnOUDt84nRPyOMzj0YiWdP6YlLS7SmA?width=2610&height=952&cropmode=none" title="Invalid_Customer_Name" alt="Invalid_Customer_Name">

* Invalid customer phone number

  <img src="https://mp4knw.bn.files.1drv.com/y4mlFrZBnEUOsZyOPM5Qd9-CltSOA6633NnrCtDbdJAgDiFtV59YqCLX05lBh7jJ9Q-7n1Q2sOQPHwC-NOzqxLBE67578eQ2ljyyMx7XXWbaTf6l9J0nJuNZwMv60UnC7XIEpqTZxMiWAok8Ds5AAQsR04l7guTOKKAgp7VmftxTJjSCPX8-piac4n2rfB5R_uecegg_Xtbj77dpTOHXKHNmw?width=2610&height=952&cropmode=none" title="Invalid_Customer_Phone_Number" alt="Invalid_Customer_Phone_Number">

* Multiple input errors
  
  If there are multiple input errors, all of them will be shown.

    <img src="https://ypautw.bn.files.1drv.com/y4m--pQODM-0259kAIzz_r7icCt2HjCtICCBilQy0AnjQFczQ0KluubHeXbZ7oK7HHoQ_4vY92A2QlOgLzwV5rnzJ22IE59ZuLDXnstyigSs0kObTxUbs-9bWA1Uqx_ahkcLRQF9Vht5ftA5PG9hhGZvjqFVTEjVwVM955pjqCT-3vAC0MkzzLz0JwU52hTylLR9r5hdaTUJ_mYICmhcC5QIA?width=2620&height=1372&cropmode=none" title="Multiple_Input_Errors" alt="Multiple_Input_Errors">

* Logging

  Errors and the corresponding input will be written in log file for reference.

  Log file path:
  
      {folder in which the application is run}/PhoneBook.log

  e.g.

  When running application directly in local:

    <img src="https://lkffcq.bn.files.1drv.com/y4mJ8rxHJomdCTQoFUCVZ3hHPpUU4wdPy-vQNiYnCxkuc4nKMV2r2te5U9b6veCasiXyCrCkWpmL5w_X-TK1AJBQ-V437NPlqQkh2yIjWVfzUj1Iz__j8gOBCufabV1H0p1I3AVzORRsGld-mhKuKLcfpmy5hDVIFkCfbf4HFFCv6Nutofv6qQVb1fYD3sqfXX5bLbEVzlM271PfNcWaP-xBg?width=2076&height=1096&cropmode=none" title="Log_In_Local" alt="Log_In_Local">

  When running application with docker:

    <img src="https://ahpiqa.bn.files.1drv.com/y4mFgjUNF9yRpO7xiNbcPkJvpR9iWsy-Bc3OPMpjNZomjO9VPBOG8mgYb_ZO0M1qvOBY-IXUyjvGBvcR0wpP7WsLxWEVSX47CuWbeNTsILLg7GOboV28nf6jHnXU8-J67zD_2q4URiIHDHdR76uIjT9aY3Xq8A61PHmd7526p4jQoWsdN_t5aVMGTTB_GT62ekt1NT14o9yx8xrAEfQ_CxUfg?width=1786&height=976&cropmode=none" title="Log_In_Container" alt="Log_In_Container">

## **Architecture**
### Application
The application is built with Java with Spring Boot framework. It serves as 2 purposes:

1. Holds a console application (by implementing CommandLineRunner)
2. Exposes API endpoints for other applications to consume (by its RestController).

The console application injects and makes use of the phoneBookService directly in the same application.

Although a web interface is not created, developers can interact with the application with the RESTful API endpoints (as described in [here](#restful-api-endpoints)), which in turns make use of phoneBookService as well.

Ideally, this provides a way for future integration to communicate and interact with the application.

### Data

Phone book data is persisted in memory as per requirement, in the form of Spring beans which are initialised as per application start up. 

2 beans, one for each of phoneBookA and phoneBookB are created. Each bean will contain a mapping of customer names to customer phone number. Map is used because of is nature that it does not allow duplicate keys, and its "put" action serves create and update (i.e. replaces the old value) at the same time.

Of the various maps, TreeMap is chosen since it gives a natural order of keys and thus gives a more organized view when displayed on screen. 

## **Design and development approach**

Test Driven Design (TDD) is adopted as much as possible in the development.

Test cases for a particular function is prepared before writing the code for it. When the development is completed, JUnit test is performed to test the service.

Afterwards the application is run on docker to check the behavior. The function is considered to be  completed afterwards, and the cycle goes on again for another function.

Development cycle:

1. Test cases for a requirement 

2. Write the code for the service

3. Test the service with JUnit

4. Write the code for the front-end (console interface in this case) to use the service 

5. Run the application in docker

6. Repeat for next requirement

## **Technology stack**

* Java 8
* Spring Boot v2.3.1
  * Libraries:

    * spring-boot-starter-web (standard web library for Spring Boot)
    * lombok (for simplifying coding of entity/model classes, and provides slf4j support for logging)
    * spring-boot-starter-test (default, for JUnit testing with JUnit 5)
    * Apache commons-lang3 (for providing util for generating random customer names/phone numbers for testing)
    * springfox-swagger2 (for generating API docs)
    * springfox-swagger-ui (for providing a UI showing API specifications)
    * spring-boot-maven-plugin (default, for integration of Maven build)
