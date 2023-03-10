#!/usr/bin/env groovy

import groovy.io.FileType

@NonCPS
def call(String folderName) {
    def list = []
    list.add('')
    def dir = new File("/inventory/${folderName}/")
    dir.eachFileRecurse (FileType.DIRECTORIES) { file ->
        list << file.name
    }
    return list.sort() - 'group_vars' 
}