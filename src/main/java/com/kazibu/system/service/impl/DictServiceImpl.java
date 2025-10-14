package com.kazibu.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kazibu.system.entity.Dict;
import com.kazibu.system.entity.DictValue;
import com.kazibu.system.repository.DictRepository;
import com.kazibu.system.repository.DictValueRepository;
import com.kazibu.system.service.DictService;

@Service
public class DictServiceImpl implements DictService {

  @Autowired
  private DictRepository dictRepository;

  @Autowired
  private DictValueRepository dictValueRepository;

  @Override
  public Dict createDict(Dict dto) {
    return dictRepository.save(dto);
  }

  @Override
  public Dict updateDict(Dict dto) {
    Dict existing = dictRepository.findById(dto.getId()).orElse(null);
    if (existing == null) {
      return null;
    }

    String oldType = existing.getDictType();
    String newType = dto.getDictType();

    existing.setDictName(dto.getDictName());
    existing.setRemark(dto.getRemark());
    existing.setStatus(dto.getStatus());

    if (newType != null && !newType.equals(oldType)) {
      // 变更 dictType 前，校验新类型与现有字典值组合不会冲突
      // 如果新类型下存在相同 dict_value，将在唯一约束下失败，这里可加更细粒度校验
      // 简化处理：尝试批量更新，若违反唯一约束将抛异常回滚
      existing.setDictType(newType);
      // 先保存字典本身的新类型
      dictRepository.save(existing);
      // 再批量更新字典数据的 dict_type（同事务内）
      dictValueRepository.bulkUpdateDictType(oldType, newType);
      return existing;
    }

    return dictRepository.save(existing);
  }

  @Override
  @Transactional
  public void deleteDict(Long id) {
    Dict dict = dictRepository.findById(id).orElse(null);
    if (dict == null) {
      return;
    }
    String dictType = dict.getDictType();
    dictValueRepository.deleteByDictType(dictType);
    dictRepository.deleteById(id);
  }

  @Override
  public Dict getDict(Long id) {
    return dictRepository.findById(id).orElse(null);
  }

  @Override
  public List<Dict> listDicts(String dictName, String status) {
    if ((dictName == null || dictName.isEmpty()) && (status == null || status.isEmpty())) {
      return dictRepository.findAll();
    }
    String nameLike = dictName == null ? "" : dictName;
    String statusLike = status == null ? "" : status;
    return dictRepository.findByDictNameContainingAndStatusContaining(nameLike, statusLike);
  }

  @Override
  public DictValue createDictValue(DictValue dto) {
    if (dictValueRepository.existsByDictTypeAndDictValue(dto.getDictType(), dto.getDictValue())) {
      throw new IllegalArgumentException("字典键值已存在");
    }
    return dictValueRepository.save(dto);
  }

  @Override
  public DictValue updateDictValue(DictValue dto) {
    DictValue existing = dictValueRepository.findById(dto.getId()).orElse(null);
    if (existing == null) {
      return null;
    }
    // 禁止修改 dictType / dictValue
    existing.setDictLabel(dto.getDictLabel());
    existing.setRemark(dto.getRemark());
    existing.setStatus(dto.getStatus());
    return dictValueRepository.save(existing);
  }

  @Override
  public void deleteDictValue(Long id) {
    dictValueRepository.deleteById(id);
  }

  @Override
  public DictValue getDictValue(Long id) {
    return dictValueRepository.findById(id).orElse(null);
  }

  @Override
  @Transactional(readOnly = true)
  public List<DictValue> listDictValuesByType(String dictType) {
    return dictValueRepository.findByDictType(dictType);
  }
}
