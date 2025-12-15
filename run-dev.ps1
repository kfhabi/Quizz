<#
Run development environment for the Quizz project on Windows PowerShell.

What it does:
- Optionally creates and starts a MySQL Docker container named `quizz-mysql` (if Docker is available and container not present)
- Exports required environment variables for Spring Boot
- Starts the Spring Boot backend using the Maven wrapper in a new PowerShell window
- Serves the front-end static files (from `src/main/resources/static/Quizapp - Copy`) on port 3000 using `npx http-server` if Node is available, or `python -m http.server` if Python is available. Each server starts in its own PowerShell window so you can see logs.

Usage:
  Right-click -> Run with PowerShell, or execute in PowerShell:
    cd C:\path\to\quizz
    .\run-dev.ps1

Requirements:
- PowerShell (Windows) (this script targets PowerShell 5.1+)
- Docker (optional, to auto-run MySQL)
- Node (optional) OR Python (for serving static front-end)

#>

Set-StrictMode -Version Latest

$root = Split-Path -Parent $MyInvocation.MyCommand.Definition
Write-Host "Project root: $root"

# 1) Start MySQL with Docker (if Docker available and container not already created)
if (Get-Command docker -ErrorAction SilentlyContinue) {
    Write-Host "Docker found — checking for container 'quizz-mysql'..."
    $exists = docker ps -a --format "{{.Names}}" | Select-String -Pattern '^quizz-mysql$' -Quiet
    if (-not $exists) {
        Write-Host "Creating and starting MySQL container 'quizz-mysql' (root / 220520, DB quizz_db)..."
        docker run --name quizz-mysql -e MYSQL_ROOT_PASSWORD=220520 -e MYSQL_DATABASE=quizz_db -p 3306:3306 -d mysql:8.0
        Start-Sleep -Seconds 6
        Write-Host "MySQL container started. Give it a few seconds to initialize if starting first time."
    } else {
        $running = docker ps --format "{{.Names}}" | Select-String -Pattern '^quizz-mysql$' -Quiet
        if (-not $running) {
            Write-Host "Starting existing container 'quizz-mysql'..."
            docker start quizz-mysql
            Start-Sleep -Seconds 3
        } else {
            Write-Host "Container 'quizz-mysql' is already running."
        }
    }
} else {
    Write-Host "Docker not found. Make sure you have a MySQL server running on localhost:3306 with database 'quizz_db'."
}

# 2) Export environment variables for Spring Boot (adjust if needed)
$env:SPRING_DATASOURCE_USERNAME = $env:SPRING_DATASOURCE_USERNAME -or 'root'
$env:SPRING_DATASOURCE_PASSWORD = $env:SPRING_DATASOURCE_PASSWORD -or '220520'
$env:JWT_SECRET = $env:JWT_SECRET -or 'dev-secret'

Write-Host "Using DB user: $($env:SPRING_DATASOURCE_USERNAME)"

# 3) Start backend (Spring Boot) in a new PowerShell window
$backendCmd = "cd `"$root`"; .\mvnw.cmd spring-boot:run"
Write-Host "Starting backend (Spring Boot) in a new window..."
Start-Process powershell -ArgumentList "-NoExit","-Command","$backendCmd"

# 4) Start frontend static server in a new PowerShell window
$frontendDir = Join-Path $root 'src\main\resources\static\Quizapp - Copy'
if (-not (Test-Path $frontendDir)) {
    Write-Host "Frontend folder not found: $frontendDir" -ForegroundColor Yellow
    Write-Host "If you want the static files served by Spring Boot, open: http://localhost:8080/Quizapp%20-%20Copy/trangchu.html"
} else {
    # Prefer Node's http-server if available
    if (Get-Command npx -ErrorAction SilentlyContinue) {
        $frontCmd = "cd `"$frontendDir`"; npx http-server -p 5500"
        Write-Host "Starting frontend with npx http-server on http://localhost:5500..."
        Start-Process powershell -ArgumentList "-NoExit","-Command","$frontCmd"
    } elseif (Get-Command python -ErrorAction SilentlyContinue) {
        $frontCmd = "cd `"$frontendDir`"; python -m http.server 5500"
        Write-Host "Starting frontend with Python http.server on http://localhost:5500..."
        Start-Process powershell -ArgumentList "-NoExit","-Command","$frontCmd"
    } else {
        Write-Host "Neither Node (npx) nor Python found. Cannot auto-start a static server." -ForegroundColor Yellow
        Write-Host "You can still access the static UI from the backend at: http://localhost:8080/Quizapp%20-%20Copy/trangchu.html"
    }
}

Write-Host "Done. Backend logs should appear in the backend PowerShell window. Frontend logs (if started) are in a separate window."
Write-Host "Backend: http://localhost:8080"
Write-Host "Frontend (dev server): http://localhost:5500/trangchu.html (if started)"
