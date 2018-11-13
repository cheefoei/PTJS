package com.TimeToWork.TimeToWork.Company;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.Database.Entity.Company;
import com.TimeToWork.TimeToWork.R;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.TimeToWork.TimeToWork.MainApplication.mRequestQueue;
import static com.TimeToWork.TimeToWork.MainApplication.root;

public class CompanyInfoFragment extends Fragment {

    private CustomProgressDialog mProgressDialog;
    private TextView tvAddress, tvPhoneNumber, tvEmail;

    private Company company;

    public CompanyInfoFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company_info, container, false);

        mProgressDialog = new CustomProgressDialog(getActivity());
        company = (Company) getArguments().getSerializable("COMPANY");

        tvAddress = (TextView) view.findViewById(R.id.tv_company_address);
        tvPhoneNumber = (TextView) view.findViewById(R.id.tv_company_phone);
        tvEmail = (TextView) view.findViewById(R.id.tv_company_email);

        getCompanyData();

        return view;
    }

    private void getCompanyData() {

        //Show progress dialog
        mProgressDialog.setMessage("Loading …");
        mProgressDialog.show();

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {

                        //Change company JSON to Array
                        JSONObject companyObject = jsonResponse.getJSONObject("COMPANY");
                        //Assign company data
                        Company company = new Company();
                        company.setAddress(companyObject.getString("company_address"));
                        company.setPhone_number(companyObject.getString("company_phone_number"));
                        company.setEmail(companyObject.getString("company_email"));
                        company.setRating(companyObject.getDouble("company_rating"));

                        tvAddress.setText(company.getAddress());
                        tvPhoneNumber.setText(company.getPhone_number());
                        tvEmail.setText(company.getEmail());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mProgressDialog.dismiss();
            }
        };

        GetCompanyRequest getCompanyRequest = new GetCompanyRequest(
                root + getString(R.string.url_get_company),
                responseListener,
                null
        );
        mRequestQueue.add(getCompanyRequest);
    }

    private class GetCompanyRequest extends StringRequest {

        private Map<String, String> params;

        GetCompanyRequest(String url,
                          Response.Listener<String> listener,
                          Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("id", company.getId());
        }

        public Map<String, String> getParams() {
            return params;
        }
    }
}
