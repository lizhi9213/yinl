package com.desjf.dsjr.fragment.bank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.desjf.dsjr.R;
import com.desjf.dsjr.adapter.bankAdapter.BankFundDetailsRechargeAdapter;
import com.desjf.dsjr.base.BaseFragment;
import com.desjf.dsjr.biz.retrofit.BankHttpUtils;
import com.desjf.dsjr.biz.retrofit.CallUtils;
import com.desjf.dsjr.model.bankModel.BankAccountFundDetailsModel;
import com.desjf.dsjr.utils.PreferenceCache;
import com.desjf.dsjr.widget.WrapContentLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//资金明细中的充值
public class BankRechargeFragment extends BaseFragment {

  private View rootView;

  @BindView(R.id.tv_zwsj)
  LinearLayout tvZwsj;
  private List<String> str_list = new ArrayList<>();
  private BankFundDetailsRechargeAdapter myFundDetailsAdapter;
  @BindView(R.id.srl_zjmx)
  SwipeRefreshLayout srlZjmx;
  @BindView(R.id.rv_account_fund_details)
  RecyclerView rvAccountFundDetails;

  private int i = 1;
  private List<BankAccountFundDetailsModel.ReturnListBean> jyRecordModelList = new ArrayList<>();
  private int lastVisibleItem;
    private LinearLayoutManager linearLayoutManager= new WrapContentLinearLayoutManager(getActivity());
    int maxCount=150;//每次加载的记录条数
    int totalNum=0;//记录总条数
    boolean ifRefresh=false;//记录是否刷新
  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

      if (null == rootView) {
          rootView = inflater.inflate(R.layout.fragment_recharge, null);
          ButterKnife.bind(this, rootView);
          getJyjl();
          initData();
      }

      ViewGroup parent = (ViewGroup)rootView.getParent();
      if (parent != null) {
          parent.removeView(rootView);
      }

//        rootView = (View) inflater.inflate(R.layout.fragment_payments, container, false);
      return rootView;
  }

  private void getJyjl() {
//
//      CallUtils.call(getActivity(), BankHttpUtils.getHttpRequestService().getUserAllMessage(PreferenceCache.getToken(), i + "", "20","B"), new CallUtils.MyCallListener() {
//              @Override
//              public void onRespnseSuccess(String jsonString) {
//                  hideLoadingDialog();
//                  BankAccountFundDetailsModel bankPresentRecordModel= JSON.parseObject(jsonString,BankAccountFundDetailsModel.class);
//                  List<BankAccountFundDetailsModel.ReturnListBean> getList=bankPresentRecordModel.getReturnList();
//                  initJy(getList);
//                  srlZjmx.setRefreshing(false);
//              }
//          });
      CallUtils.Refreshcall(getActivity(), BankHttpUtils.getHttpRequestService().getUserAllMessage(PreferenceCache.getToken(), i + "", ""+maxCount,"B"), new CallUtils.MyCallListener() {
          @Override
          public void onRespnseSuccess(String jsonString) {

              BankAccountFundDetailsModel bankPresentRecordModel= JSON.parseObject(jsonString,BankAccountFundDetailsModel.class);
              if (bankPresentRecordModel.getNum()!=null) {
                  totalNum= Integer.parseInt(bankPresentRecordModel.getNum());
              }
              List<BankAccountFundDetailsModel.ReturnListBean> getList=bankPresentRecordModel.getReturnList();
              initJy(getList);
              srlZjmx.setRefreshing(false);
          }
      },srlZjmx);
  }

  private void initJy(List<BankAccountFundDetailsModel.ReturnListBean> jyRecordModels) {
//      linearLayoutManager = new LinearLayoutManager(getActivity());
      if(jyRecordModels==null){
          srlZjmx.setVisibility(View.GONE);
          tvZwsj.setVisibility(View.VISIBLE);
      }else {
          if(ifRefresh){
              jyRecordModelList.clear();
          }
          jyRecordModelList.addAll(jyRecordModels);
          if (jyRecordModelList.size() != 0) {
              if (i == 1) {
                  myFundDetailsAdapter = new BankFundDetailsRechargeAdapter(jyRecordModelList, getActivity());
                  rvAccountFundDetails.setAdapter(myFundDetailsAdapter);
                  rvAccountFundDetails.setLayoutManager(linearLayoutManager);
                  myFundDetailsAdapter.notifyDataSetChanged();
              } else {
                  myFundDetailsAdapter.addData(jyRecordModels);
                  myFundDetailsAdapter.notifyDataSetChanged();
              }
              tvZwsj.setVisibility(View.GONE);
              srlZjmx.setVisibility(View.VISIBLE);
          } else {
              srlZjmx.setVisibility(View.GONE);
              tvZwsj.setVisibility(View.VISIBLE);
          }
      }
  }

  private void initData() {
      srlZjmx.setColorSchemeResources(R.color.main);

      srlZjmx.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {
              i = 1;
              ifRefresh=true;
              getJyjl();
          }
      });
      //上拉加载更多
      rvAccountFundDetails.setOnScrollListener(new RecyclerView.OnScrollListener() {
          @Override
          public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
              super.onScrollStateChanged(recyclerView, newState);
              if (newState == RecyclerView.SCROLL_STATE_IDLE
                      && lastVisibleItem + 1 == myFundDetailsAdapter.getItemCount()&&myFundDetailsAdapter.getItemCount()>=149) {
                  i++;
                  ifRefresh=false;
                  //如果当前i小于总页数，则加载更多
                  if(i<=getMaxPageNum(totalNum,maxCount)) {
                      getJyjl();
                  }
              }
          }

          @Override
          public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
              super.onScrolled(recyclerView, dx, dy);
              lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
          }
      });
  }

    //计算最大页数
    private int getMaxPageNum(int totalNumber,int maxCount){
        return (totalNumber%maxCount==0)?(totalNumber/maxCount):(totalNumber/maxCount+1);
    }
}