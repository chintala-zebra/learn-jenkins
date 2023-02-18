#!/usr/bin/env groovy

def info(message) {
    echo "\033[31m INFO: ${message} \033[0m "
}

def warning(message) {
    echo "WARNING: ${message}"
}
