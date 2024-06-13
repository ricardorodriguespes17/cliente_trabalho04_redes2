/* ***************************************************************
* Autor............: Ricardo Rodrigues Neto
* Matricula........: 201710560
* Inicio...........: 13/06/2024
* Ultima alteracao.: 13/06/2024
* Nome.............: ReadFile
* Funcao...........: Le um arquivo de texto 'config.txt'
*************************************************************** */

package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadFile {

  public ReadFile() {
  }

  public List<String> readFile(String pathname) {
    try {
      List<String> lines = new ArrayList<String>();
      File file = new File(pathname);
      Scanner scanner = new Scanner(file);
      while (scanner.hasNext()) {
        String item = scanner.next();
        lines.add(item.replaceAll(";", " ").trim());
      }
      scanner.close();
      return lines;
    } catch (FileNotFoundException e) {
      System.out.println("> Arquivo não encontrado.");
      return new ArrayList<String>();
    }
  }

  public String getVariable(String variable) {
    ReadFile readFile = new ReadFile();

    List<String> lines = readFile.readFile("config.txt");

    for (String line : lines) {
      String[] lineSplited = line.split("=");
      if (lineSplited[0].equals(variable)) {
        return lineSplited[1].trim();
      }

    }
    return null;
  }
}
