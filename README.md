# EcommerceWb — Test Automation Suite

This repository contains an automated test suite for an e-commerce web/API application using Java, TestNG, RestAssured and Allure for reporting. The project is built with Maven and contains tests across multiple layers (unit, integration, API, UI, and end-to-end).

## Project scope
This repository contains automated tests for the whole project, not only E2E scenarios. The test types included (or intended) are:
- API tests — using RestAssured to validate HTTP endpoints and contracts
- UI tests — Selenium/WebDriver-based browser tests that take screenshots on failure
- End-to-end (E2E) scenarios — full workflows that exercise multiple layers
- Smoke/regression groups — lightweight checks and full regression suites

## Quick overview
- Language: Java 17 (as configured in `pom.xml`, maven.compiler.source/target)
- Test runner: TestNG 7.11.0
- Build: Maven
- Reporting: Allure 2.24.0 (results under `target/allure-results`)
- API tests: RestAssured 5.5.6
- UI tests: Selenium 4.35.0 (WebDriver, screenshots saved under `screenshots/`)

## Prerequisites
- Java 17 (JDK installed and `JAVA_HOME` set)
- Maven 3.6+
- (Optional) Allure commandline for serving reports locally — recommended Allure version: 2.24.0 (see https://docs.qameta.io/allure/)

On Windows (PowerShell) you can verify:

```powershell
java -version
mvn -version
```

## How tests are organized
Conventional locations in this project:
- `src/test/java` — test code. Typical package breakdown:
  - `tests` — TestNG test classes (grouped by feature/suite)
  - `api` — API clients / request builders / DTOs
  - `ui` or `pages` — Page objects and UI helpers
  - `utils` — helpers, test data readers, common utilities
- `src/test/resources` — test data, TestNG XML suites, config files
- `screenshots/` — screenshots captured by UI tests
- `target/allure-results/` — Allure test results (generated artifacts)

If you are adding a new test, place it under the appropriate package (api/ui/tests) and follow existing naming conventions (e.g., *FeatureName*Test or *FeatureName*E2ETest).

## How to run tests
From the repository root (where `pom.xml` lives) run in PowerShell:

```powershell
# Run the whole test suite
mvn clean test
```

Running specific suites or configurations:

```powershell
# Run tests defined by a TestNG XML file
mvn -DtestngXmlFile=testng.xml test

# Run tests by TestNG groups (if groups are used in annotations)
mvn -Dgroups=smoke test

# Run a single test class
mvn -Dtest=com.mycompany.tests.MyFeatureTest test

# Run a single test method within a class
mvn -Dtest=com.mycompany.tests.MyFeatureTest#shouldDoX test
```

Passing environment/configuration at runtime:

```powershell
# Examples of system properties you can use; these depend on how the tests read them
mvn -Denv=qa -DbaseUrl=https://example.com -Dbrowser=chrome test
```

Maven profiles and custom goals
- Some projects provide Maven profiles (e.g., `-Pui`, `-Papi`) to select different test sets or to enable WebDriver dependencies; check `pom.xml` for profiles in this repo and use them when available.

## Allure reporting
This project produces Allure results under `target/allure-results/`.

If you have Allure CLI installed, generate and serve a report from the repo root:

```powershell
# Generate and open Allure report (requires allure installed)
allure serve target/allure-results
```

Or generate HTML to `target/site/allure` and open it:

```powershell
allure generate target/allure-results -o target/site/allure --clean
Start-Process target/site/allure/index.html
```

If you don't have the Allure CLI, the generated `target/allure-results` folder can be uploaded to CI that supports Allure or viewed using the Allure service.

## Running fast/local iterations
- For speedy local work, run only the tests you need with `-Dtest=` or `-Dgroups=` instead of the full suite.
- Consider using `mvn -DskipTests=false -Dtest=... -T1C test` to reduce test execution overhead in CI or locally (adjust threads as appropriate).

## Test data and configuration
- Test data (fixtures) are loaded from `src/test/resources` or via utility classes under `utils/`.
- Environment-specific configuration (base URLs, credentials) may be kept in resource files or system properties; check `pom.xml` and `src/test/resources` for properties you can override.
- Secrets should not be checked into the repo. Use environment variables or CI secret management and pass them as system properties.

## Adding new tests (short guide)
1. Create a new test class under `src/test/java` in the correct package.
2. Use descriptive TestNG class/method names and annotate groups (`@Test(groups = {"smoke"})`) where appropriate.
3. Place data files in `src/test/resources` or create a data provider.
4. Capture screenshots on failures (there should be a helper method in `utils` — follow the existing implementation).
5. Add Allure annotations if you want richer reports (e.g., `@Severity`, `@Feature`, `@Story`).
6. Run locally and verify `target/allure-results` contains results.

## Project structure (high level)
- `src/test/java` — test code (TestNG tests, page objects, API wrappers)
- `src/test/resources` — test resources and test data
- `target/` — Maven output (compiled tests, `allure-results`, surefire reports)
- `screenshots/` — UI test screenshots captured during test runs

## Project map (visual)
Below is a simple visual map of the repository to help you quickly find important files and folders.

```
EcommerceWb/
├─ pom.xml                      # Maven build file and dependency management
├─ README.md                    # This documentation
├─ testng.xml                   # TestNG suite configuration (if present)
├─ src/
│  ├─ main/
│  │  ├─ java/                  # (optional) application source code
│  │  └─ resources/             # (optional) main resources
│  └─ test/
│     ├─ java/                  # Test source code
│     │  ├─ api/                # API clients, request builders, DTOs
│     │  ├─ ui/                 # Page objects and UI tests
│     │  ├─ tests/              # TestNG test classes (E2E, integration, unit)
│     │  └─ utils/              # Helpers, data readers, test utilities
│     └─ resources/             # Test data and configuration files
├─ screenshots/                 # Captured screenshots from UI tests
├─ target/                      # Build output (generated by Maven)
│  ├─ classes/
│  ├─ test-classes/
│  ├─ surefire-reports/         # TestNG/Surefire reports
│  └─ allure-results/           # Allure JSON results and attachments
└─ .gitignore (recommended)     # Ignore build artifacts and large files
```

Notes:
- If you add new folders or change naming conventions, keep this map updated for easier onboarding.
- Consider adding a `.gitignore` with `target/`, `screenshots/` and other generated artifacts if you don't want to commit them.

## CI integration notes
Typical CI pipeline steps for this project:
1. Checkout code
2. Restore/cache Maven local repository (if supported by CI)
3. Set up JDK and any browser drivers needed for UI tests
4. Run `mvn -B clean test` (non-interactive batch mode)
5. Publish `target/allure-results` and optionally generate or publish Allure report

Adjust pipelines to only run UI tests when a suitable environment (headless browser/driver) is available.

## Common tasks / Troubleshooting
- If tests fail due to missing dependencies, ensure a stable internet connection and run `mvn -U clean test`.
- If Allure report shows empty results, confirm `target/allure-results/` contains `.json` and attachment files after a test run.
- If a test requires environment variables or external services, set them as system properties via Maven:

```powershell
mvn -Denv=qa -DbaseUrl=https://example.com test
```

- To collect more detailed logs, enable debug output for Maven:

```powershell
mvn -X -Dtest=com.mycompany.tests.MyFeatureTest test
```

- If UI tests fail due to driver errors, ensure the browser driver binary (chromedriver/geckodriver) version matches browser used in CI or local machine.

## Contributing
1. Create a branch for your changes.
2. Run the full test suite (or targeted tests) locally: `mvn clean test`.
3. Add/modify tests and update documentation if needed.
4. Open a pull request with a descriptive title and testing notes.

## Notes
- Screenshots and generated artifacts are kept in the repo's `screenshots/` and `target/` directories. Consider adding them to `.gitignore` if you prefer not to commit large generated files.

## Contact / Maintainer
If you need help running the tests or adapting them to CI, open an issue or contact the repository owner.

---

Requirements coverage:
- Create a README.md in the project root: Done
- Include run instructions, prerequisites, and reporting info: Done
