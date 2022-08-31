*** Settings ***
Library    RequestsLibrary

*** Variables ***
${baseUrl}  http://13.214.202.16:8080

*** Test Cases ***
Request GET API Testing
  create session      usersession     ${baseUrl}
  ${response}=    get on session     usersession     /api/teachers
  ${status_response}=  convert to string   ${response.status_code}
  ${body_content}=    convert to string  ${response.content}
  should be equal      ${status_response}    200

