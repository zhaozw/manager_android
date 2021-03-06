package org.com.manager.response;

import android.content.Context;
import android.widget.Toast;

import org.apache.http.Header;
import org.com.manager.bean.ApiErrorCodeEnum;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

public class AsyncApiResponseHandler extends JsonHttpResponseHandler {
    Context context;

    public AsyncApiResponseHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
            String ret = response.getString("ret");
            if ("SUCCESS".equals(ret)) {
                onApiResponse(response);
            } else {
                String errMsg = response.getString("errMsg");
                ApiErrorCodeEnum apiErrorCodeEnum = getErrorMsg(errMsg);
                if (apiErrorCodeEnum.getErrorId() == "1") {
                    if (errMsg==null||errMsg.isEmpty()){
                        errMsg="访问失败";
                    }
                    Toast.makeText(context, errMsg,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context,
                            "错误代码：" + apiErrorCodeEnum.getErrorId() + " "
                                    + apiErrorCodeEnum.getErrorDescription() + " ",
                            Toast.LENGTH_SHORT).show();
                }
                onApiResponse(null);
            }
        } catch (JSONException e) {
            Toast.makeText(context,
                    "解析json数据失败，请检查对象类型",
                    Toast.LENGTH_SHORT).show();
            onApiResponse(null);
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        Toast.makeText(context,
                "访问api失败,请检查客户端是否连接电脑分出的wifi",
                Toast.LENGTH_SHORT).show();
        onApiResponse(null);
    }

    /**
     * 获得errorCode
     */
    private ApiErrorCodeEnum getErrorMsg(String errmsg) {
        ApiErrorCodeEnum apiErrorCode = ApiErrorCodeEnum.UNKNOWN;
        for (ApiErrorCodeEnum apiErrorCodeEnum : ApiErrorCodeEnum.values()) {
            if (apiErrorCodeEnum.getErrorId().equals(errmsg)) {
                apiErrorCode = apiErrorCodeEnum;
                break;
            }
        }
        return apiErrorCode;
    }

    public void onApiResponse(JSONObject response) {

    }

}
