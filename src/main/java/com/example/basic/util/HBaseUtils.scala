package com.example.basic.util

import org.apache.hadoop.hbase.{CellUtil, HBaseConfiguration, KeyValue, TableName}
import org.apache.hadoop.hbase.client.{ConnectionFactory, Delete, Get}
import org.apache.hadoop.hbase.util.Bytes

import scala.collection.JavaConversions._

object HBaseUtils {
  private val conf = HBaseConfiguration.create()
  private val conn = ConnectionFactory.createConnection(conf)

  private def getTable(tableName: String) = {
    conn.getTable(TableName.valueOf(tableName))
  }

  def getRow(tableName: String, rowkey: String): Map[String, String] = {
    val table = getTable(tableName)
    val get = new Get(rowkey.getBytes())
    val result = table.get(get)
    if (result.isEmpty) {
      Map.empty[String, String]
    } else {
      val map = asScalaIterator(result.listCells().iterator())
        .map(cell => Bytes.toString(CellUtil.cloneQualifier(cell)) -> Bytes.toString(CellUtil.cloneValue(cell)))
        .toMap
      map
    }
  }

  def deleteColumns(tableName: String, rowkey: String,colfamily: String, cols: List[String]): Unit = {
    val table = getTable(tableName)

    val delete = new Delete(rowkey.getBytes())
    cols.foreach(col => delete.addColumn(colfamily.getBytes(), col.getBytes()))
    table.delete(delete)
  }

}
