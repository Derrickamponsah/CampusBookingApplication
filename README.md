# Campus Booking Application - Deployment Guide

This repository is ready for deployment on **Render** using Docker.

## Project Structure Changes

The project has been refactored for containerization:
- **Frontend integrated**: All templates and static assets have been moved into `backend/src/main/resources` so they are bundled within the JAR.
- **Dockerized**: A multi-stage `Dockerfile` is included in the root directory.
- **Render Setup**: A `render.yaml` file is provided for easy deployment via Render Blueprints.

## Local Development

To run the application locally:

1.  Navigate to the `backend` directory: `cd backend`
2.  Ensure you have a `.env` file with your database password (this file is ignored by Git).
3.  Run the application: `./mvnw spring-boot:run`

## Deployment on Render

### Step 1: Push Changes to GitHub
Ensure all recent changes (Dockerfile, render.yaml, etc.) are pushed to your repository.

### Step 2: Create a Render Blueprint
1.  Log in to [Render](https://render.com).
2.  Click **New +** and select **Blueprint**.
3.  Connect your GitHub repository.
4.  Render will automatically detect the `render.yaml` file.
5.  In the configuration, add your `DB_PASSWORD` environment variable when prompted.

### Step 3: Database Configuration
The application is configured to connect to your Aiven PostgreSQL database. Ensure the database is running and accessible.

## Environment Variables
- `DB_PASSWORD`: The password for your Aiven database.

## Troubleshooting
If you experience issues during deployment:
- Check the **Events** and **Logs** tabs on Render.
- Verify that the Docker build completes successfully.
- Ensure the database SSL mode is set correctly in `application.properties` (currently set to `require`).
