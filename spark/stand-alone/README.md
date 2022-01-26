# Spark alone mode 
- from https://github.com/bitnami/bitnami-docker-spark

# minimum config 

- set master - worker 
```
spark:
  ...
  environment:
    - SPARK_MODE=master   # worker 
```

- set url master (only need in worker)
```
SPARK_MASTER_URL = spark://spark-master:7077 (default)
```

# note 

