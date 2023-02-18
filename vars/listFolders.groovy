#!/usr/bin/env groovy

import groovy.io.FileType

def call(String folderName) {
    def list = []
    list.add('')
    def dir = new File("/inventory/${folderName}/")
    dir.eachFileRecurse (FileType.DIRECTORIES) { file ->
        echo file.name
        list << file.name
    }
    return list.sort() - 'group_vars' 
}
