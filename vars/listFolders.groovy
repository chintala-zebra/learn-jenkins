#!/usr/bin/env groovy

import groovy.io.FileType

def call(String folderName) {
    def list = []
    list.add('')
    def dir = new File("/inventory/${folderName}")
    dir.eachFile (FileType.FILES) { file ->
        list << file.name.replaceAll('.yml','');
    }
    return list
}
