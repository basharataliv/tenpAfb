INSERT INTO EMAIL_TEMPLATE (id,EMAIL_BCC,EMAIL_CC,EMAIL_SUBJECT,EMAIL_TO,IS_ACTIVE,TEMPLATE_CONTENT,TEMPLATE_NAME) VALUES
	 (1,NULL,N'admin@afba.com',N'EMSIINBND Validation Failed',N'admin@afba.com',1,N'<body><h2 style="text-align: center;text-transform: uppercase;color: #fc0330;">Image Plus Alert !</h2><p style="text-align: justify;letter-spacing: 1px;">This is to inform that Image Plus System failed in validation of records in EMSIINBND with policy id: <p th:text="${policyId}"></p> Please Review !!</p><br><p>Regards<br>AFBA Monitoring Service</p></body>',N'EMAIL_EMSIINBND_VALIDATION_FAILURE'),
	 (2,NULL,NULL,N'DDPROCESS Transaction failed',N'admin@afba.com',1,N'<body><h2 style="text-align: center;text-transform: uppercase;color: #fc0330;">Image Plus Alert !</h2><p style="text-align: justify;letter-spacing: 1px;">DDPROCESS failed to process transaction with ID: <b th:text="${transactionId}"></b></p><p><b>Error: </b><span th:text="${errorMessage}"></span></p><br><p>Regards<br>AFBA Monitoring Service</p></body>',N'EMAIL_DDPROCESS_FAILURE'),
     (3,NULL,N'admin@afba.com.com,admin@afba.com',N'AFBA - SharepointControl Alert',N'admin@afba.com',1,N'<body><h2 style="text-align: center;text-transform: uppercase;color: #fc0330;">Image Plus Alert !</h2><p style="text-align: justify;letter-spacing: 1px;">This is to inform that Image Plus System has detected low available storage on Sharepoint. Please add more Site Libraries in Sharepoint Control in order to avoid Document insertion failure. Following is the state of remaining libraries,</p><table width="80%" border=''1'' style="width:80%!important;border-collapse:collapse''"><tr><th>Site Name</th><th>Library Name</th><th>Files Count</th></tr><tr th:each="record: ${sharepointControlRecords}"><td th:text="${record.siteName}" /><td th:text="${record.libraryName}" /><td th:text="${record.filesCount}" /></tr></table><br><p>Regards<br>AFBA Monitoring Service</p></body>',N'EMAIL_SHAREPOINT_CONTROL'),
	 (4,NULL,N'admin@afba.com',N'AFBA - EKDMOVE Error Alert',N'admin@afba.com',1,N'<body>
  <h2 style="text-align: center;text-transform: uppercase;color: #fc0330;">Image Plus Alert !</h2>
  <p style="text-align: justify;letter-spacing: 1px;">This is to inform that Image Plus System has detected error in EKDMOVE batch job. Please check the details,</p>
  <table width="80%" border=''1'' style="width:80%!important;border-collapse:collapse''">
    <tr>
      <th>Case Id</th>
      <th>Policy Id</th>
      <th>Queue Id</th>
    </tr>
    <tr th:each="record: ${ekdMoveDatails}">
      <td th:text="${record.caseId}" />
      <td th:text="${record.policyId}" />
      <td th:text="${record.queue}" />
    </tr>
  </table>
  <br>
  <p>Regards <br>AFBA Monitoring Service </p>
</body>',N'EMAIL_EKDMOVE_ERROR');