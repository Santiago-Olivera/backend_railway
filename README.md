# Tech Trend Emporium

Tech Trend Emporium is a backend application for a online store. It serves as the backend for managing products, user accounts, and orders.

## Development Rules

### Trunk-Based Development

Trunk-Based Development (TBD) is a software development approach where all developers work on a single branch, often referred to as the "trunk" or "main" branch. This approach promotes collaboration and allows for faster integration of code changes.

#### Main Branch

- **Blocked for Direct Commits**: The main branch is blocked for direct commits. All changes must be made through feature branches.
- **Pull Request Process**: Developers create short-lived feature branches for their changes. Once the changes are complete, they create a pull request (PR) to merge the feature branch into the main branch.
- **Approval Process**: All pull requests require the approval of at least two developers. This ensures that code changes are reviewed and tested before being merged into the main branch.

## Pipelines

### Continuous Integration (CI)

Our CI pipeline automates the build, test, and release process for our application.

- **Build and Test**: The CI pipeline builds the application and runs unit tests to ensure code quality.
- **DockerHub**: We use DockerHub to store Docker images generated during the CI process. This allows us to easily deploy the application in different environments.

### Continuous Deployment (CD)

Our CD pipeline automates the deployment process for new changes to the main branch.

- **Build and Upload to AWS ECR**: The CD pipeline builds the Docker image and uploads it to an AWS Elastic Container Registry (ECR). This ensures that the latest version of the application is available for deployment.
- **Deploy to AWS ECS**: The CD pipeline deploys the Docker container to an AWS Elastic Container Service (ECS) cluster. This ensures that the application is running and accessible to users.

## Database

### Relational Database (Amazon RDS)

We use Amazon RDS (Relational Database Service) for our database.

- **Managed Service**: RDS is a managed database service that simplifies database administration tasks such as provisioning, patching, and backup.

## Swagger Documentation

### Swagger

Our API documentation is available through Swagger.

- **API Documsentation**: Swagger provides a user-friendly interface for exploring and testing our APIs.
- **URL**: [Swagger documentation](http://18.218.100.42:8080/doc/swagger-ui/index.html#/)

## Testing

### Unit Testing

We have implemented comprehensive unit tests for our application using JUnit and Mockito.

- **Coverage Metrics**: Our unit tests have achieved 91% coverage in controllers, 94% coverage in services, and 80% coverage overall. This high level of coverage ensures that our code is well-tested and reliable.
- **Mockito**: Mockito is a popular Java mocking framework that we use to mock dependencies in our unit tests. Mockito allows us to isolate components for testing and simulate the behavior of dependencies, ensuring that our tests are focused and efficient.

## Wiki documentation

Our project includes comprehensive documentation in the wiki section, with 12 pages detailing our features and implementation strategies. These pages offer a thorough explanation of how our application works and how each feature is implemented, providing valuable insights for developers and contributors.

## Postman

In addition to the wiki documentation, we have included a Postman collection in the repository. This collection contains a JSON file that can be imported into Postman to easily test all our API endpoints. We have also provided a README file in the same directory to guide users on how to import and use the collection effectively.