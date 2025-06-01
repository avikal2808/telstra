# Test script for SIM card activation API

Write-Host "Starting SIM card activation API tests..." -ForegroundColor Green

# Define base URL
$baseUrl = "http://localhost:8080/api/sim"

# Test 1: Activate a SIM card
$activationBody = @{
    iccid = "89314404000055555555"
    customerEmail = "test@example.com"
} | ConvertTo-Json

Write-Host "`nTest 1: Activating SIM card..." -ForegroundColor Cyan
try {
    $activationResponse = Invoke-RestMethod -Uri "$baseUrl/activate" -Method Post -Body $activationBody -ContentType "application/json"
    Write-Host "Activation Response: $activationResponse" -ForegroundColor Green
}
catch {
    Write-Host "Activation Error: $_" -ForegroundColor Red
}

# Sleep to allow for processing
Start-Sleep -Seconds 2

# Test 2: Query SIM card activation by ID
Write-Host "`nTest 2: Querying SIM card activation by ID 1..." -ForegroundColor Cyan
try {
    $queryResponse = Invoke-RestMethod -Uri "$baseUrl/query?simCardId=1" -Method Get
    Write-Host "Query Response:" -ForegroundColor Green
    Write-Host "ICCID: $($queryResponse.iccid)" -ForegroundColor Green
    Write-Host "Customer Email: $($queryResponse.customerEmail)" -ForegroundColor Green
    Write-Host "Active: $($queryResponse.active)" -ForegroundColor Green
}
catch {
    Write-Host "Query Error: $_" -ForegroundColor Red
}

Write-Host "`nAPI tests completed." -ForegroundColor Green
