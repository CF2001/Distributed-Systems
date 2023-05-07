#!/bin/bash

# set the number of iterations
num_iterations=100

# set the timeout for each iteration in seconds
timeout=20s

# set the counter for failed executions to 0
failed_executions=0

# loop through the number of iterations and run the Java program
for i in $(seq 1 $num_iterations); do
  echo "Running iteration $i..."
  
  # run the Java program with the timeout command and capture the exit code
  timeout $timeout java main.HeistToTheMuseum
  exit_code=$?
  
  # check if the exit code indicates a failed execution
  if [ $exit_code -ne 0 ]; then
    echo "Iteration $i failed with exit code $exit_code"
    failed_executions=$((failed_executions + 1))
  fi
  
done

# print the number of failed executions
echo "Number of failed executions: $failed_executions"

