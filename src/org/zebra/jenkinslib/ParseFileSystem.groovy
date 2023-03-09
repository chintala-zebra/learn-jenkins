package org.zebra.jenkinslib

  public class ParseFileSystem {

    def getFolders(String parent) {
      def list = []
      list.add('')
      def dir = new File("/inventory/${parent}/")
      dir.eachFile (FileType.DIRECTORIES) { file ->
          list << file.name
      }
      return list.sort() - 'group_vars' 
  }
}