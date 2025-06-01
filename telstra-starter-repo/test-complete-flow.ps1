# Comprehensive test script for SIM card activation system with database persistence

Write-Host "Starting complete test flow for SIM card activation system..." -ForegroundColor Green

# Step 1: Start the mock actuator service on port 8444 (in a new PowerShell window)
Write-Host "`nStarting mock actuator service on port 8444..." -ForegroundColor Cyan
Start-Process powershell -ArgumentList "-Command `"cd '$PWD'; mvn spring-boot:run -Dspring-boot.run.main-class=au.com.telstra.simcardactivator.mock.MockActuatorApplication`"" -WindowStyle Normal

# Wait for the mock actuator to start
Write-Host "Waiting for mock actuator to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

# Step 2: Start the main application on port 8080 (in a new PowerShell window)
Write-Host "`nStarting main application on port 8080..." -ForegroundColor Cyan
Start-Process powershell -ArgumentList "-Command `"cd '$PWD'; mvn spring-boot:run`"" -WindowStyle Normal

# Wait for the main application to start
Write-Host "Waiting for main application to start..." -ForegroundColor Yellow
Start-Sleep -Seconds 15

# Step 3: Test the activation endpoint (POST /api/sim/activate)
Write-Host "`nTesting SIM card activation endpoint..." -ForegroundColor Cyan

# Test with a valid ICCID (starting with 8931 for success)
$validIccid = "89314404000055555555"
$validActivationBody = @{
    iccid = $validIccid
    customerEmail = "test@example.com"
} | ConvertTo-Json

Write-Host "`nTest 3.1: Activating SIM card with valid ICCID: $validIccid" -ForegroundColor Cyan
try {
    $validActivationResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/sim/activate" -Method Post -Body $validActivationBody -ContentType "application/json"
    Write-Host "Valid Activation Response: $validActivationResponse" -ForegroundColor Green
}
catch {
    Write-Host "Valid Activation Error: $_" -ForegroundColor Red
}

# Test with an invalid ICCID (not starting with 8931 for failure)
$invalidIccid = "12345678901234567890"
$invalidActivationBody = @{
    iccid = $invalidIccid
    customerEmail = "test@example.com"
} | ConvertTo-Json

Write-Host "`nTest 3.2: Activating SIM card with invalid ICCID: $invalidIccid" -ForegroundColor Cyan
try {
    $invalidActivationResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/sim/activate" -Method Post -Body $invalidActivationBody -ContentType "application/json"
    Write-Host "Invalid Activation Response: $invalidActivationResponse" -ForegroundColor Green
}
catch {
    Write-Host "Invalid Activation Error: $_" -ForegroundColor Red
}

# Wait for database records to be persisted
Write-Host "Waiting for database records to be persisted..." -ForegroundColor Yellow
Start-Sleep -Seconds 5

# Step 4: Test the query endpoint (GET /api/sim/query)
Write-Host "`nTesting SIM card query endpoint..." -ForegroundColor Cyan

# Query for the first record (successful activation)
Write-Host "`nTest 4.1: Querying SIM card activation with ID: 1" -ForegroundColor Cyan
try {
    $validQueryResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/sim/query?simCardId=1" -Method Get
    Write-Host "Valid Query Response:" -ForegroundColor Green
    Write-Host "ICCID: $($validQueryResponse.iccid)" -ForegroundColor Green
    Write-Host "Customer Email: $($validQueryResponse.customerEmail)" -ForegroundColor Green
    Write-Host "Active: $($validQueryResponse.active)" -ForegroundColor Green
}
catch {
    Write-Host "Valid Query Error: $_" -ForegroundColor Red
}

# Query for the second record (failed activation)
Write-Host "`nTest 4.2: Querying SIM card activation with ID: 2" -ForegroundColor Cyan
try {
    $invalidQueryResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/sim/query?simCardId=2" -Method Get
    Write-Host "Invalid Query Response:" -ForegroundColor Green
    Write-Host "ICCID: $($invalidQueryResponse.iccid)" -ForegroundColor Green
    Write-Host "Customer Email: $($invalidQueryResponse.customerEmail)" -ForegroundColor Green
    Write-Host "Active: $($invalidQueryResponse.active)" -ForegroundColor Green
}
catch {
    Write-Host "Invalid Query Error: $_" -ForegroundColor Red
}

Write-Host "`nComplete test flow finished." -ForegroundColor Green
Write-Host "Note: The application and mock actuator are still running in separate PowerShell windows." -ForegroundColor Yellow
Write-Host "You can access the H2 console at http://localhost:8080/h2-console to view the database records." -ForegroundColor Yellow
Write-Host "JDBC URL: jdbc:h2:mem:simcarddb, Username: sa, Password: password" -ForegroundColor Yellow
