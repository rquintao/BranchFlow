# BranchFlow

BranchFlow is a command-line tool offering a new approach to Git workflows.

## Features

- Initialize a new BranchFlow repository
- Display help and available commands
- Show the current version

## Requirements

- Java 8 or higher
- Maven

## Build Instructions

1. **Clone the repository**  
   ```sh
   cd BranchFlow
   ```

2. **Build the project**  
   ```sh
   mvn clean package
   ```

   The executable JAR will be created in the `target/` directory.

## Usage

Run the application using:

```sh
./branchflow
```

### Available Commands

- `init` &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Initialize a new BranchFlow repository
- `help` &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Show help message
- `version` &nbsp;&nbsp;Show the version of BranchFlow

**Example:**

```sh
./branchflow help
```

## Development

- Main class: `com.branchflow.App`
- Source code: `src/main/java/com/branchflow/App.java`
