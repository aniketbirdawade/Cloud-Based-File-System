# Cloud Connection – S3 File Upload/Download Service

A simple Spring Boot REST API for uploading and downloading files to/from an AWS S3 bucket.

## Features

- **Upload** files to an S3 bucket via a REST endpoint
- **Download** files from the S3 bucket by filename
- Built with the AWS SDK v2 (`software.amazon.awssdk`)

## Tech Stack

- Java
- Spring Boot (Spring Web, Spring MVC)
- AWS SDK for Java v2 (`s3` module)

## Project Structure

```
com.Cloud.Connection
├── FileController.java   # REST endpoints: /upload, /download
├── FileService.java      # Business logic for S3 put/get operations
└── S3Config.java         # S3Client bean configuration
```

## Prerequisites

- Java 17+ (or your project's target JDK version)
- Maven or Gradle
- An AWS account with an S3 bucket
- AWS credentials with `s3:PutObject` and `s3:GetObject` permissions on the bucket

## Configuration

The S3 bucket name and AWS region are currently set in code:

- Bucket name: `cloud-based-file-system` (in `FileService.java`)
- Region: `AP_SOUTHEAST_2` (in `S3Config.java`)

> **⚠️ Security note:** `S3Config.java` currently hardcodes AWS access and secret keys directly in source code. This is **not recommended for production** — credentials committed to source control can be leaked. Instead, use one of:
> - Environment variables (`AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`) with the SDK's `DefaultCredentialsProvider`
> - `application.properties` / `application.yml` values injected via `@Value`, sourced from environment variables or a secrets manager
> - An IAM role (if deploying on EC2/ECS/EKS) via `InstanceProfileCredentialsProvider` or the default provider chain
>
> Also consider externalizing the bucket name and region into `application.properties` rather than hardcoding them.

Example `application.properties` approach:

```properties
aws.accessKey=${AWS_ACCESS_KEY_ID}
aws.secretKey=${AWS_SECRET_ACCESS_KEY}
aws.region=ap-southeast-2
aws.bucketName=cloud-based-file-system
```

## Running the Application

```bash
# Using Maven
mvn spring-boot:run

# Or build and run the jar
mvn clean package
java -jar target/<your-artifact-name>.jar
```

By default, Spring Boot runs on `http://localhost:8080`.

## API Endpoints

### Upload a File

```
POST /upload
Content-Type: multipart/form-data
```

**Form field:** `file` — the file to upload

**Example (cURL):**
```bash
curl -X POST http://localhost:8080/upload \
  -F "file=@/path/to/your/file.txt"
```

**Response:** `"Uploaded Successfully"` (or an error message on failure)

### Download a File

```
GET /download?fileName={fileName}
```

**Example (cURL):**
```bash
curl -X GET "http://localhost:8080/download?fileName=file.txt" \
  --output downloaded-file.txt
```

**Response:** The raw file bytes, with `Content-Disposition: attachment` so browsers/clients will prompt a download.

## Known Limitations / Suggested Improvements

- **Hardcoded credentials & bucket name** — move to environment variables or a secrets manager (see Configuration section above).
- **No file name sanitization** — `fileName` is used directly in the `Content-Disposition` header and as the S3 key, which could allow header injection or path traversal-like key names. Validate/sanitize input before use.
- **No content-type detection** — the download response doesn't set `Content-Type`, so clients may not render the file correctly. Consider using `Files.probeContentType` or storing/retrieving the content type as S3 object metadata.
- **Generic exception handling** — `uploadFile` returns the raw exception message to the client, which can leak internal details. Consider logging server-side and returning generic error responses instead.
- **No file size/type restrictions** — consider adding validation (max file size, allowed extensions) before uploading to S3.
- **No authentication/authorization** — the endpoints are currently open. Add security (e.g., Spring Security) before exposing this publicly.

## License

Add your license of choice here.
