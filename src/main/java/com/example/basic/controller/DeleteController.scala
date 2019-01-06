package com.example.basic.controller

import java.util

import com.example.basic.util.{GenUtils, HBaseUtils}
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.{RequestMapping, RequestParam, ResponseBody, RestController}

@Controller
class DeleteController {
  val nameSpace = "zxpt_ixn:"


  @RequestMapping(Array())
  def homepage(model: Model): String = {
    val map = formatOut(Map.empty[String, String])
    model.addAttribute("items", map)
    "table"
  }

  @RequestMapping(Array("query"))
  def queryIndustry(@RequestParam(value="name", required = false)name: String, model: Model) = {
    val newName = GenUtils.process(name)
    model.addAttribute("inputname", newName)
    getIxnbm(newName) match {
      case Some(ixnbm) => {
        val map = HBaseUtils.getRow(nameSpace + "zxpt_qyxx_aisino", ixnbm)
        val linkedHashMap = formatOut(map)

        model.addAttribute("items", linkedHashMap)
      }
      case None => {
        val map = Map[String, String]()
        val linked = formatOut(map)
        model.addAttribute("items", linked)
        model.addAttribute("isExists", "该公司名称不存在!")
      }
    }
    "table"
  }

  @RequestMapping(Array("delete"))
  def deleteIndustry(@RequestParam("name")name: String, model: Model): String = {
    val newName = GenUtils.process(name)
    val cols = List("hyzldm_1", "hyzldm_1_mc", "hyzldm_2", "hyzldm_2_mc", "hyzldm", "hyzldm_mc", "hymx", "hymx_mc")
    getIxnbm(newName) match {
      case Some(ixnbm) => {
        HBaseUtils.deleteColumns(nameSpace + "zxpt_qyxx_aisino", ixnbm, "qyxx", cols)
        model.addAttribute("isDelete", "行业删除成功!")
      }
      case None =>
    }

    "forward:/query"
  }


  private def getIxnbm(name: String) = {
    val map = HBaseUtils.getRow(nameSpace + "zxpt_qyxx_mc", name)
    map.get("key")
  }

  private def formatOut(map: Map[String, String]): java.util.Map[String, String] = {
    val res = new util.LinkedHashMap[String, String]()
    res.put("公司名称", map.getOrElse("mc", ""))
    res.put("纳税人识别号", map.getOrElse("tyshxydm", ""))
    res.put("行业门类", map.getOrElse("hyzldm_1", "") + "  " + map.getOrElse("hyzldm_1_mc", ""))
    res.put("行业大类", map.getOrElse("hyzldm_2", "") + "  " + map.getOrElse("hyzldm_2_mc", ""))
    res.put("行业中类", map.getOrElse("hyzldm", "") + "  " + map.getOrElse("hyzldm_mc", ""))
    res.put("行业小类", map.getOrElse("hymx", "") + "  " + map.getOrElse("hymx_mc", ""))
    res
  }





}
