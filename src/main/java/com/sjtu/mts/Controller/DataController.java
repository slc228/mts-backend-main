package com.sjtu.mts.Controller;

import com.sjtu.mts.Entity.Cluster;
import com.sjtu.mts.Entity.Data;
import com.sjtu.mts.EventTrack.EventTreeNode;
import com.sjtu.mts.Keyword.KeywordResponse;
import com.sjtu.mts.Repository.DataRepository;
import com.sjtu.mts.Response.*;
import com.sjtu.mts.Service.*;
import com.sjtu.mts.WeiboTrack.WeiboRepostTree;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/data")
@CrossOrigin()
public class DataController {

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private SearchService searchService;

    @Autowired
    private WeiboTrackService weiboTrackService;

    @Autowired
    private TextClassService textClassService;

    @Autowired
    private EventTrackService eventTrackService;

    @Autowired
    private SentimentService sentimentService;

    @Autowired
    private TextAlertService textAlertService;

    @Autowired
    private SummaryService summaryService;

    @GetMapping("/testApi")
    @ResponseBody
    public String heartBeating() {
        return "healthy";
    }

    @GetMapping("/findByCflag/{cflag}")
    @ResponseBody
    public List<Data> findById(@PathVariable("cflag") int cflag) {
        return dataRepository.findByCflag(String.valueOf(cflag));
    }

    @GetMapping("/globalSearch/searchByUser")
    @ResponseBody
    public DataResponse searchByUser (
            @RequestParam("fid") long fid,
            @RequestParam("username") String username,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("pageId") int pageId
    ) throws UnsupportedEncodingException {
        return searchService.searchByUser(fid, username, pageSize, pageId);
    }

    @GetMapping("/getActivateUser")
    @ResponseBody
    public Map<String, Integer> getActivateUser(@RequestParam("fid") long fid) {
        return searchService.getActivateUser(fid);
    }

    @GetMapping("/globalSearch/dataSearch")
    @ResponseBody
    public DataResponse findByKeywordAndCflagAndPublishedDayAndFromType(
            @RequestParam("keyword") String keyword,
            @RequestParam("cflag") String cflag,
            @RequestParam("startPublishedDay") String startPublishedDay,
            @RequestParam("endPublishedDay") String endPublishedDay,
            @RequestParam("fromType") String fromType,
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("timeOrder") int timeOrder
    ) {
        String decodeKeyword = "";
        try{
            decodeKeyword = java.net.URLDecoder.decode(keyword, "utf-8");
            System.out.println("Decoded keyword: "+decodeKeyword);
        }catch (Exception e){
            System.out.println(e);
        }
        return searchService.Search(decodeKeyword, cflag, startPublishedDay, endPublishedDay, fromType,
                page, pageSize, timeOrder);
    }

    @GetMapping("/globalSearch/dataSearchWithObject")
    @ResponseBody
    public DataResponse dataSearchWithObject(
            @RequestParam("keyword") String keyword,
            @RequestParam("cflag") String cflag,
            @RequestParam("startPublishedDay") String startPublishedDay,
            @RequestParam("endPublishedDay") String endPublishedDay,
            @RequestParam("fromType") String fromType,
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("timeOrder") int timeOrder,
            @RequestParam("keywords") String keywords
    ) {
        String decodeKeyword = "";
        String decodeKeywords = "";
        try{
            decodeKeyword = java.net.URLDecoder.decode(keyword, "utf-8");
            decodeKeywords = java.net.URLDecoder.decode(keywords, "utf-8");
            System.out.println("Decoded keyword: "+decodeKeyword);
            System.out.println(decodeKeywords);
        }catch (Exception e){
            System.out.println(e);
        }
        return searchService.SearchWithObject(decodeKeyword, cflag, startPublishedDay, endPublishedDay, fromType,page, pageSize, timeOrder,decodeKeywords);
    }

    @GetMapping("/globalSearch/resourceCount")
    @ResponseBody
    public ResourceCountResponse countByKeywordAndPublishedDayAndFromType(
            @RequestParam("keyword") String keyword,
            @RequestParam("startPublishedDay") String startPublishedDay,
            @RequestParam("endPublishedDay") String endPublishedDay
    ) {
        return searchService.globalSearchResourceCount(keyword, startPublishedDay,
                endPublishedDay);
    }
    @GetMapping("/globalSearch/resourceCount2")
    @ResponseBody
    public ResourceCountResponse countByKeywordAndPublishedDayAndFromType2(
            @RequestParam("fid") long fid,
            @RequestParam("startPublishedDay") String startPublishedDay,
            @RequestParam("endPublishedDay") String endPublishedDay

    ) {
        return searchService.globalSearchResourceCountByFid(fid,startPublishedDay,endPublishedDay);
    }
    @GetMapping("/globalSearch/cflagCount")
    @ResponseBody
    public CflagCountResponse countByKeywordAndPublishedDayAndCflag(
            @RequestParam("keyword") String keyword,
            @RequestParam("startPublishedDay") String startPublishedDay,
            @RequestParam("endPublishedDay") String endPublishedDay
    ) {
        return searchService.globalSearchCflagCount(keyword, startPublishedDay, endPublishedDay);
    }

    @GetMapping("/globalSearch/cflagCount2")
    @ResponseBody
    public CflagCountResponse countByKeywordAndPublishedDayAndCflag2(
            @RequestParam("fid") long fid,
            @RequestParam("startPublishedDay") String startPublishedDay,
            @RequestParam("endPublishedDay") String endPublishedDay

    ) {
        return searchService.globalSearchCflagCountByFid(fid,startPublishedDay,endPublishedDay);
    }
    @GetMapping("/globalSearch/amountTrendCount")
    @ResponseBody
    public AmountTrendResponse countAmountTrendByKeywordAndPublishedDay(
            @RequestParam("keyword") String keyword,
            @RequestParam("startPublishedDay") String startPublishedDay,
            @RequestParam("endPublishedDay") String endPublishedDay
    ) {
        return searchService.globalSearchTrendCount(keyword, startPublishedDay, endPublishedDay);
    }
    @GetMapping("/globalSearch/amountTrendCount2")
    @ResponseBody
    public AmountTrendResponse countAmountTrendByKeywordAndPublishedDay2(
            @RequestParam("fid") long fid,
            @RequestParam("startPublishedDay") String startPublishedDay,
            @RequestParam("endPublishedDay") String endPublishedDay

    ) {
        return searchService.globalSearchTrendCount2(fid,startPublishedDay,endPublishedDay);
    }

    @GetMapping("/globalSearch/amountTrendCount3")
    @ResponseBody
    public AmountTrendResponse countAmountTrendByKeywordAndPublishedDay3(
            @RequestParam("fid") long fid,
            @RequestParam("startPublishedDay") String startPublishedDay,
            @RequestParam("endPublishedDay") String endPublishedDay
    ) {
        return searchService.globalSearchTrendCount3(fid,startPublishedDay,endPublishedDay);
    }

    /*????????????????????????
    @author FYR
     */
    @GetMapping("/globalSearch/areaCount")
    @ResponseBody
    public AreaAnalysisResponse countAreaByKeywordAndPublishedDay(
            @RequestParam("keyword") String keyword,
            @RequestParam("startPublishedDay") String startPublishedDay,
            @RequestParam("endPublishedDay") String endPublishedDay
    ) {
        return searchService.countArea(keyword,startPublishedDay,endPublishedDay);
    }
    @GetMapping("/globalSearch/areaCount2")
    @ResponseBody
    public AreaAnalysisResponse countAreaByKeywordAndPublishedDay2(
            @RequestParam("fid") long fid,
            @RequestParam("startPublishedDay") String startPublishedDay,
            @RequestParam("endPublishedDay") String endPublishedDay

    ) {
        return searchService.countAreaByFid(fid,startPublishedDay,endPublishedDay);
    }

    /*????????????????????????
    @author FYR
     */
    @GetMapping("/singleSearch/findByFangAn")
    @ResponseBody
    public DataResponse fangAnSearch(
            @RequestParam("fid") long fid,
            @RequestParam("cflag") String cflag,
            @RequestParam("startPublishedDay") String startPublishedDay,
            @RequestParam("endPublishedDay") String endPublishedDay,
            @RequestParam("fromType") String fromType,
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("timeOrder") int timeOrder


    ){
        return searchService.fangAnSearch(fid,cflag,startPublishedDay,endPublishedDay,fromType,page,pageSize,timeOrder);
    }
    /*
    * ??????????????????????????????
    * @author FYR*/
    @GetMapping("/singleSearch/findByFangAn2")
    @ResponseBody
    public DataResponse fangAnSearch2(
            @RequestParam("fid") long fid,
            @RequestParam("keyword")String keyword,
            @RequestParam("cflag") String cflag,
            @RequestParam("startPublishedDay") String startPublishedDay,
            @RequestParam("endPublishedDay") String endPublishedDay,
            @RequestParam("fromType") String fromType,
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize,
            @RequestParam("timeOrder") int timeOrder
    ){
        String decodeKeyword = "";
        try{
             decodeKeyword = java.net.URLDecoder.decode(keyword, "utf-8");
             System.out.println(decodeKeyword);
        }catch (Exception e){
            System.out.println(e);
        }

        return searchService.fangAnSearch2(fid,decodeKeyword,cflag,startPublishedDay,endPublishedDay,fromType,page,pageSize,timeOrder);
    }

    /*???????????????????????????????????????????????????
    @author Ma Baowei
     */
    @GetMapping("/weiboTrack")
    @ResponseBody
    public WeiboRepostTree trackWeiboByKeywordAndPublishedDay(
            @RequestParam("fid") long fid,
            @RequestParam("startPublishedDay") String startPublishedDay,
            @RequestParam("endPublishedDay") String endPublishedDay
    ) {
        return weiboTrackService.trackWeibo(fid,startPublishedDay,endPublishedDay);
    }

    /*??????text???????????????
    @author Fu Yongrui
     */
    @PostMapping(value = "/sensitiveWord")
    @ResponseBody
    public JSONArray sensitiveWord(@RequestBody Map<String,String> textinfo )
    {
        //return searchService.sensitiveWordFiltering(textinfo.get("text"));
        return searchService.sensitiveWordFilteringHanLp(textinfo.get("text"));
    }

    /*?????????????????????????????????
    @author Fu Yongrui
     */
    @RequestMapping(value = "/sensitive")
    @ResponseBody
    public JSONArray sensitiveWord(@RequestParam("fid") long fid,
                                   @RequestParam("startPublishedDay") String startPublishedDay,
                                   @RequestParam("endPublishedDay") String endPublishedDay)
    {
        return searchService.sensitiveWord(fid,startPublishedDay,endPublishedDay);
    }
    /*???????????????
        @author Fu Yongrui
         */
    @PostMapping(value = "/addSensitive")
    @ResponseBody
    public JSONObject addSensitiveWord(@RequestBody Map<String,String> sensitiveWordInfo)

    {
        return searchService.addSensitiveWord(sensitiveWordInfo.get("sensitiveWord"));
    }
    /*
    * ???????????????
    * @author Fu Yongrui
    */
    @RequestMapping(value = "/delSensitive")
    @ResponseBody
    public JSONObject delSensitiveWord(@RequestParam("sensitiveWord") String sensitiveWord)
    {
        return searchService.delSensitiveWord(sensitiveWord);
    }

    /*???????????????
    @author Ma Baowei
     */
    @GetMapping("/keywordExtraction")
    @ResponseBody
    public List<KeywordResponse> extractKeyWordByFidAndPublishedDay(
            @RequestParam("fid") long fid,
            @RequestParam("startPublishedDay") String startPublishedDay,
            @RequestParam("endPublishedDay") String endPublishedDay,
            @RequestParam("keywordNumber") int keywordNumber,
            @RequestParam("extractMethod") String extractMethod
    ) {
        return searchService.extractKeyword(fid,startPublishedDay,endPublishedDay,keywordNumber,extractMethod);
    }

    /*????????????
    @author Fu Yongrui
     */
    @RequestMapping(value = "/textClass")
    @ResponseBody
    public JSONArray textClass(@RequestParam("fid") long fid,
                               @RequestParam("startPublishedDay") String startPublishedDay,
                               @RequestParam("endPublishedDay") String endPublishedDay)
    {
        return textClassService.textClass(fid,startPublishedDay,endPublishedDay);
    }

    @RequestMapping(value = "/sensitiveCount")
    @ResponseBody
    public com.alibaba.fastjson.JSONObject sensitiveCount(@RequestParam("fid") long fid)
    {
        return textAlertService.sensitiveCount(fid);
    }

    /*????????????
    @author HZT
     */
    @PostMapping(value = "/textAlert")
    @ResponseBody
    public com.alibaba.fastjson.JSONObject textAlert(@RequestBody Map<String,List<String>> textInfo)
    {
        System.out.println(textInfo.get("textList"));
        return textAlertService.textAlert(textInfo.get("textList"));
    }


    /*????????????2???????????????????????????
    @author Fu Yongrui
     */
    @PostMapping(value = "/textClass2")
    @ResponseBody
    public com.alibaba.fastjson.JSONObject textClass2(@RequestBody Map<String,List<String>> textInfo)
    {
        return textClassService.textClass2(textInfo.get("textList"));
    }
    /*????????????
    @author Fu Yongrui
     */
    @RequestMapping(value = "/textClustering")
    @ResponseBody
    public JSONArray textClustering(@RequestParam("fid") long fid,
                               @RequestParam("startPublishedDay") String startPublishedDay,
                               @RequestParam("endPublishedDay") String endPublishedDay)
    {
        return textClassService.clustering(fid,startPublishedDay,endPublishedDay);
    }
    /*????????????2,????????????????????????
    @author Fu Yongrui
     */
    @RequestMapping(value = "/clusteringData")
    @ResponseBody
    public List<Cluster> clusteringData(@RequestParam("fid") long fid,
                                        @RequestParam("startPublishedDay") String startPublishedDay,
                                        @RequestParam("endPublishedDay") String endPublishedDay)
    {
        return textClassService.clusteringData(fid,startPublishedDay,endPublishedDay);
    }
    /*???????????????
    @author Huang Sicheng
     */
    @RequestMapping(value = "/singleDocumentSummary")
    @ResponseBody
    public JSONObject singleDocumentSummary(@RequestParam("document") String document)
    {
        return summaryService.singleSummary(document);
    }
    /*???????????????
    @author Huang Sicheng
     */
    @RequestMapping(value = "/multiDocumentSummary")
    @ResponseBody
    public JSONObject multiDocumentSummary(@RequestParam("fid") long fid,
                                           @RequestParam("startPublishedDay") String startPublishedDay,
                                           @RequestParam("endPublishedDay") String endPublishedDay)
    {
        return summaryService.multiSummary(fid, startPublishedDay, endPublishedDay);
    }
    /*???????????????????????????????????????
    @author Ma Baowei
     */
    @RequestMapping(value = "/getEventTree")
    @ResponseBody
    public EventTreeNode getEventTree(@RequestParam("fid") long fid,
                                      @RequestParam("startPublishedDay") String startPublishedDay,
                                      @RequestParam("endPublishedDay") String endPublishedDay)
    {
        return eventTrackService.getEventTree(fid,startPublishedDay,endPublishedDay);
    }
    /*????????????
    @author Ma Baowei
     */
    @PostMapping(value = "/sentimentAnalysis")
    @ResponseBody
    public com.alibaba.fastjson.JSONObject predictSentiment(@RequestBody Map<String,List<String>> textInfo)
    {
        return sentimentService.sentimentPredict(textInfo.get("textList"));
    }
    /*????????????????????????????????????
    @author Ma Baowei
     */
    @GetMapping(value = "/sentimentCount")
    @ResponseBody
    public SentimentCountResponse sentimentCount(@RequestParam("fid") long fid,
                                                   @RequestParam("startPublishedDay") String startPublishedDay,
                                                   @RequestParam("endPublishedDay") String endPublishedDay)
    {
        return sentimentService.countSentiment(fid,startPublishedDay,endPublishedDay);
    }
    /*??????????????????
    @author Ma Baowei
     */
    @GetMapping(value = "/sentimentTrendCount")
    @ResponseBody
    public SentimentTrendResponse sentimentTrendCount(@RequestParam("fid") long fid,
                                                 @RequestParam("startPublishedDay") String startPublishedDay,
                                                 @RequestParam("endPublishedDay") String endPublishedDay)
    {
        return sentimentService.sentimentTrendCount(fid,startPublishedDay,endPublishedDay);
    }
    /*???????????????????????????
    @author Fu Yongrui
     */
    @PostMapping(value = "/autoaddEkeyword")
    @ResponseBody
    public JSONObject autoaddEkeyword(@RequestBody Map<String,String> textinfo
                                      ){
        return searchService.autoaddEkeyword(Long.parseLong(textinfo.get("fid")),textinfo.get("text"));
    }
    /*????????????????????????
    @author Fu Yongrui
     */
    @PostMapping(value = "/addSensitivewordForFid")
    @ResponseBody
    public JSONObject addSensitivewordForFid(@RequestBody Map<String,String> textinfo
    ){
        return searchService.addSensitivewordForFid(Long.parseLong(textinfo.get("fid")),textinfo.get("text"));
    }
    /*?????????????????????
    @author Fu Yongrui
     */
    @GetMapping(value = "/sensitivewordForFid")
    @ResponseBody
    public JSONArray sensitivewordForFid(@RequestParam("fid") long fid
    ){
        return searchService.sensitivewordForFid(fid);
    }
    /*????????????????????????
    @author Fu Yongrui
     */
    @PostMapping(value = "/sensitiveWordByFid")
    @ResponseBody
    public JSONArray sensitiveWordByFid(@RequestBody Map<String,String> textinfo )
    {
        //return searchService.sensitiveWordFiltering(textinfo.get("text"));
        return searchService.sensitiveWordByFid(Long.parseLong(textinfo.get("fid")),textinfo.get("text"));
    }

    /*????????????????????????
    @author Sun liangchen
     */
    @PostMapping(value = "/eventKeyWordByFid")
    @ResponseBody
    public JSONArray eventKeyWordByFid(@RequestBody Map<String,String> textinfo
    ){
        return searchService.eventKeyWordByFid(Long.parseLong(textinfo.get("fid")));
    }

    /*??????????????????
        @author Sun liangchen
    */
    @GetMapping("/getHotArticle")
    @ResponseBody
    public HotArticleResponse getHotArticle(
            @RequestParam("pageId") int pageId,
            @RequestParam("pageSize") int pageSize
    ){
        return searchService.getHotArticle(pageId,pageSize);
    }

    /*???????????????????????????id?????????
       @author Sun liangchen
   */
    @GetMapping("/searchByWeiboUser")
    @ResponseBody
    public JSONObject searchByWeiboUser (
            @RequestParam("fid") long fid,
            @RequestParam("WeiboUserForSearch") String WeiboUserForSearch
    ) throws UnsupportedEncodingException {
        return searchService.searchByWeiboUser(fid, WeiboUserForSearch);
    }

}
