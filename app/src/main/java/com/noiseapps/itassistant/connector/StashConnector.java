package com.noiseapps.itassistant.connector;

import com.noiseapps.itassistant.api.StashAPI;
import com.noiseapps.itassistant.database.PreferencesDAO;
import com.noiseapps.itassistant.model.account.BaseAccount;
import com.noiseapps.itassistant.model.atlassian.PagedApiModel;
import com.noiseapps.itassistant.model.stash.projects.BranchModel;
import com.noiseapps.itassistant.model.stash.projects.NewBranchModel;
import com.noiseapps.itassistant.model.stash.projects.ProjectRepos;
import com.noiseapps.itassistant.model.stash.projects.UserProjects;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.SupposeBackground;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Path;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@EBean(scope = EBean.Scope.Singleton)
public class StashConnector {

    @Bean
    PreferencesDAO preferencesDAO;

    private BaseAccount currentConfig;
    private StashAPI apiService;

    public UserProjects getProjects() {
        try {
            return apiService.getProjects();
        } catch (RetrofitError error) {
            return null;
        }
    }

    public Observable<UserProjects> reactiveGetProjects() {
        return apiService.reactiveGetProjects();
    }

    public Observable<ProjectRepos> getProjectRepos(String projectKey) {
        return apiService.getProjectRepos(projectKey);
    }

    public Observable<Object> getRepoDetails(String projectKey, String repoSlug) {
        return apiService.getRepoDetails(projectKey, repoSlug);
    }

    @SupposeBackground
    public List<BranchModel> getBranches(@Path("projectKey") String projectKey, @Path("repoSlug") String repoSlug) {
        try {
            int start = 0;
            PagedApiModel<BranchModel> branches;
            final List<BranchModel> repoBranches = new ArrayList<>();
            do {
                branches = apiService.getBranches(projectKey, repoSlug, start);
                repoBranches.addAll(branches.getValues());
                start = branches.getStart() + branches.getLimit();
            } while (!branches.isLastPage());
            return repoBranches;
        } catch (RetrofitError error) {
            return new ArrayList<>();
        }
    }

    public Observable<BranchModel> createBranch(@Path("projectKey") String projectKey, @Path("repoSlug") String repoSlug, NewBranchModel newBranchModel) {
        return apiService.createBranch(projectKey, repoSlug, newBranchModel).
                observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io());
    }


    @AfterInject

    void init() {
        initApiService();
    }

    private void initApiService() {
        if (currentConfig == null) {
            return;
        }
        final RestAdapter adapter = new RestAdapter.Builder().
                setRequestInterceptor(new Interceptor()).
                setLogLevel(RestAdapter.LogLevel.FULL).
                setErrorHandler(new JiraErrorHandler()).
                setEndpoint(currentConfig.getUrl()).build();
        apiService = adapter.create(StashAPI.class);
    }

    public void setCurrentConfig(BaseAccount newConfig) {
        currentConfig = newConfig;
        initApiService();
    }

    private class JiraErrorHandler implements ErrorHandler {

        @Override
        public Throwable handleError(RetrofitError cause) {
            Logger.e(cause, cause.getMessage());
            final Response response = cause.getResponse();
            if (response != null) {
                switch (response.getStatus()) {
                    case Consts.HTTP_NOT_AUTHORIZED:
                        break;
                    case Consts.HTTP_FORBIDDEN:
                        break;
                    case Consts.HTTP_SERVER_ERROR:
                        break;
                    default:
                }
            }
            return cause;
        }
    }

    private class Interceptor implements RequestInterceptor {

        @Override
        public void intercept(RequestFacade request) {
            addAuthenticationHeader(request);
        }

        private void addAuthenticationHeader(RequestFacade request) {
            final String basicAuthEncoded = currentConfig.getToken();
            request.addHeader(Consts.AUTH_HEADER, String.format(Consts.AUTH_HEADER_VALUE, basicAuthEncoded));
        }
    }

}
