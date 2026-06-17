# SmartBank Railway Deployment Script
# Run this after: railway login

Write-Host "=== Creating Railway Project ===" -ForegroundColor Cyan
railway project create smartbank-enterprise

Write-Host "=== Creating PostgreSQL Plugin ===" -ForegroundColor Cyan
railway add postgres

Write-Host "=== Deploying Auth Service ===" -ForegroundColor Cyan
Set-Location "C:\Users\rapha\projects\smartbank-enterprise-platform\auth-service"
railway up --service auth-service --detach

Write-Host "=== Deploying API Gateway ===" -ForegroundColor Cyan
Set-Location "C:\Users\rapha\projects\smartbank-enterprise-platform\api-gateway"
railway up --service api-gateway --detach

Write-Host "=== Setting Environment Variables ===" -ForegroundColor Cyan
railway variables set JWT_SECRET=smartbank-demo-secret-2026
railway variables set APP_ENV=production
railway variables set SERVER_PORT=8080

Write-Host "=== Getting Deployed URLs ===" -ForegroundColor Cyan
railway domain --service auth-service
railway domain --service api-gateway

Write-Host "=== Testing Health Check ===" -ForegroundColor Cyan
$gatewayUrl = railway domain --service api-gateway 2>&1
Invoke-RestMethod -Uri "$gatewayUrl/actuator/health" | ConvertTo-Json

Write-Host "=== Done ===" -ForegroundColor Green
Write-Host "Update the README with the actual deployed URLs from above."
