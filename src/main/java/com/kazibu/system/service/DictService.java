package com.kazibu.system.service;

import java.util.List;

import com.kazibu.system.entity.Dict;
import com.kazibu.system.entity.DictValue;

public interface DictService {
  // Dict
  Dict createDict(Dict dto);

  Dict updateDict(Dict dto);

  void deleteDict(Long id);

  Dict getDict(Long id);

  List<Dict> listDicts(String dictName, String status);

  // Dict Value
  DictValue createDictValue(DictValue dto);

  DictValue updateDictValue(DictValue dto);

  void deleteDictValue(Long id);

  DictValue getDictValue(Long id);

  List<DictValue> listDictValuesByType(String dictType);
}
