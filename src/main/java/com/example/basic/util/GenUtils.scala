package com.example.basic.util

object GenUtils {
  def process(name: String): String = {
    name.trim().replace("(", "（").replace(")", "）")
  }

}
