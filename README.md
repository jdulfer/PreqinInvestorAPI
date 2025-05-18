# PreqinInvestorAPI

### Architectural Overview

This RESTful API is built off of the Kotlin Ktor framework. It uses SQLite to store `Investor` and `Commitment` tables.
The data from `data.csv` has been processed by a Kotlin Notebook file (`investor-data-cleaning.ipynb`) which reads the
data in, separates it into Investor data and Commitment data, and then writes them to their own CSV files. The data has
then been read into the SQLite DB file labelled `PreqinDB.db`, which is used by the API reads from and provides to the
front end.

### Running the API
You can either run the application through the `Application.kt` main function, or with `./gradlew run`.

### Resources

- `GET: /hello`
    - Returns Hello World. Good to check it's working as intended
- `GET: /investors`
    - Returns all the `Investors` and their total commitment amount
- `GET: /investors/:investorId/commitments`
    - Returns all the `Commitments` for a particular `inevstorId`