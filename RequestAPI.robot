*** Settings ***
Library    RequestsLibrary
Library    Collections
Library    JSONLibrary

*** Variables ***
${baseUrl}  http://13.214.202.16:8080
${firstname}=   John
${middleName}=   Gray
${lastName}=   Shelby
${birthdate}=   09/21/1916
${department}=   English
${id}=   630ecaaffda09e28db70bbb1



*** Test Cases ***
Request GET API Testing
  create session      usersession     ${baseUrl}
  ${response}=    get on session     usersession     /api/teachers
  ${status_response}=  convert to string   ${response.status_code}
  ${body_content}=    convert to string  ${response.content}
  #Validation
  should be equal      ${status_response}    200
  should contain    ${body_content}     ${firstname}
  should contain    ${body_content}     ${middleName}
  should contain    ${body_content}     ${lastName}
  should contain    ${body_content}     ${birthdate}
  should contain    ${body_content}     ${department}
  should contain    ${body_content}     ${id}


POST API Testing
     create session     mysession     ${baseUrl}
     ${endpoint}    set variable    /api/teachers
     ${body}=   create dictionary   firstName=Johnny    middleName=Grayman  lastName=Shelby     birthdate=09/21/1916    department=Japanese
     ${header}=    create dictionary    Content-Type=application/json
     ${response}=     post on session    mysession    ${endpoint}      json=${body}    headers=${header}
     #Validation
     ${status_response}=  convert to string   ${response.status_code}
      should be equal    ${status_response}   201
     ${json_response}=  convert string to json  ${response.content}
     should not be empty     ${json_response}
     log to console     ${json_response}

PUT API Testing
     create session     putmysession     https://reqres.in
     ${endpoint}    set variable    /api/users/2
     ${body}=   create dictionary   name=Mor    job=Tester
     ${header}=    create dictionary    Content-Type=application/json
     ${response}=     put on session    putmysession    ${endpoint}      json=${body}    headers=${header}
     #Validation
     ${status_response}=  convert to string   ${response.status_code}
      should be equal    ${status_response}   200


DELETE API Testing
     create session     mysession     ${baseUrl}
     ${endpoint}    set variable    /api/teachers/1
     ${response}=     delete on session     mysession    ${endpoint}
     #Validation
     ${status_response}=  convert to string   ${response.status_code}
      should be equal    ${status_response}   200







