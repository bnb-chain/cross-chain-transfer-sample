# Cross Chain Transfer Java Sample

## Install Binance Chain java-sdk dependency

1. Get Binance Chain `java-sdk`

    ```shell script
    git clone https://github.com/binance-chain/java-sdk.git
    git checkout bsc_support
    ```

2. Add the following config to `java-sdk` `pom.xml`

    ```xml
        <repositories>
            <repository>
                <id>jcenter</id>
                <url>https://maven.aliyun.com/repository/public</url>
            </repository>
        </repositories>
    ```

3. Install `java-sdk` to maven local

    ```shell script
    mvn install -DskipTests
    ```
   
## Run Samples

Replace your private key and mnemonic in `App.java`, then you can run these samples.
