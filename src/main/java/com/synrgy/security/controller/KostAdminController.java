package com.synrgy.security.controller;

import com.synrgy.security.dto.BannerModel;
import com.synrgy.security.entity.Banner;
import com.synrgy.security.repository.BannerRepository;
import com.synrgy.security.service.impl.KostServiceImpl;
import com.synrgy.security.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
@Slf4j
public class KostAdminController {

    @Autowired
    public Response res;

    @Autowired
    public KostServiceImpl kostServiceImpl;


    @Autowired
    public Response response;
    @Autowired
    private BannerRepository bannerRepo;

    @PostMapping(value = {"/banner"})
    public ResponseEntity<Map> postBanner(@ModelAttribute BannerModel bannerModel) {
        Map<String, Object> resp = new HashMap<>();
        try {
            Banner banner = new Banner();
            banner.setBannerName(bannerModel.getBannerName());
            banner.setBannerImage(kostServiceImpl.uploadFile(bannerModel.getBannerImage(), "banner_image"));
            Banner saveBanner = bannerRepo.save(banner);

            return new ResponseEntity<>(res.resSuccess(saveBanner, "Success edit kost!", 200), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(res.clientError(e.getMessage()), HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping(value = {"/banner/{id}"})
    public ResponseEntity<Map> getBannerById(@PathVariable(value = "id") Long id) {
        Map<String, Object> resp = new HashMap<>();
        try {
            Optional<Banner> banner = bannerRepo.findById(id);
            if (banner.isPresent()) {
                Banner getBanner = bannerRepo.getById(id);
                return new ResponseEntity(response.resSuccess(getBanner, "Success get Banner", 200), HttpStatus.OK);
            }
            resp.put("message", "Data cannot be found");
            resp.put("status", 404);
            return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            resp.put("message", e.getMessage());
            resp.put("status", 500);
            log.error("ERROR has been found! because : {}", e.getMessage());
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = {"/banner/list"})
    public ResponseEntity<Map> getListBanner() {
        try {
            List<Banner> list = null;
            list = bannerRepo.findAll();
            return new ResponseEntity<Map>(response.resSuccess(list, "Success get list banner", 200), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(res.clientError(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/banner/{id}")
    public ResponseEntity<Map<String, Object>> deleteBannerById(@PathVariable Long id) {
        try {
            Optional<Banner> banner = bannerRepo.findById(id);

            if (banner.isPresent()) {
                banner.get().setDeletedAt(new Timestamp(System.currentTimeMillis()).toLocalDateTime());
                Banner bannerDeleted = bannerRepo.save(banner.get());
                return new ResponseEntity<>(res.resSuccess(bannerDeleted, "success", 200), HttpStatus.OK);
            }
            return new ResponseEntity<>(res.notFoundError("Banner doesn't exist"), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(res.internalServerError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
