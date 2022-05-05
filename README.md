# Warehouse API

Warehouse API is a REST API service to manage all warehouse needs in the project scope. It was developed during IT Bootcamp - Wave 5 as a part of the last group project.

## Endpoints

See also the OpenAPI Specification (/swagger-ui.html while running).

- Base API Port: 8083
- Base Database Port: 5435

| Method   | URI       | Description    |
| :---------- | :--------- | :----------------------- |
| `POST` | `/api/v1/warehouse` | Register new Warehouse |
| `GET` | `/api/v1/warehouse/{warehouseId}` | Find Warehouse by ID |
| `POST` | `/api/v1/agent` | Register a new warehouse Agent |
| `POST` | `/api/v1/section/{sectionId}/product` | Register a new association of a Product to a Section |
| `GET` | `/api/v1/section/{sectionId}` | Find Section by ID |
| `POST` | `/api/v1/fresh-products/inboundorder` | Register a new Inbound Order |
| `PUT` | `/api/v1/fresh-products/inboundorder` | Updates Batch Stock list of a Inbound Order |
| `GET` | `/api/v1/fresh-products/warehouse/{productId}` | Search for in-stock products by warehouses |
| `GET` | `/api/v1/fresh-products/due-date` | Search for in-stock products based on due date |
| `GET` | `/api/v1/fresh-products/list` | Search for all in-stock products with valid shelf life |
| `POST` | `/api/v1/fresh-products/` | Withdraw products from stock |

## Requirements

- Java 11 and later

## Installation and Usage

Use the given Maven Wrapper and Docker to build a new service container

```bash
## 1. Clone project to local 
git clone https://github.com/Grupo9-ITBootcampMeli/pi-ml-warehouse

## 2. Use Maven Wrapper to generate a new build  
./mvnw clean package

## 2.1. Optionally, build without tests 
./mvnw clean package -DskipTests

## 3. Start service via Docker 
docker-compose up

## 3.1. If you are recreating a container, build a new Docker image and delete the previous
docker-compose build --no-cache && docker-compose up -d && docker rmi -f $(docker images -f "dangling=true" -q)

```

For full operation, it is necessary to have the [Gandalf](https://github.com/Grupo9-ITBootcampMeli/pi-ml-gandalf) and the [Products](https://github.com/Grupo9-ITBootcampMeli/pi-ml-products) services running with their default ports, hostnames in the same network.
Also, Warehouse service is required for full operation of the [Cart](https://github.com/Grupo9-ITBootcampMeli/pi-ml-cart) service.

## Authors
- [Amanda Zara](https://github.com/azfernandes)
- [André Veziane](https://github.com/andrevezi)
- [Antônio Schappo](https://github.com/antonio-schappo)
- [Guilherme Pereira](https://github.com/GuiSilva23)
- [Joan Silva](https://github.com/joanmeli)
- [Vinicius Brito](https://github.com/ViniCBrito)