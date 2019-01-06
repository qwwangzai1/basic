package com.example.basic.test


object examin {
  def main(args: Array[String]): Unit = {
    val str  = " dsafk(kasjd)aklds  "
    println(process(str))
  }

  def process(name: String) = {
    name.trim().replace("(", "（").replace(")", "）")
  }

}
