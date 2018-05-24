package com.webiphany.simplecustomtypewithlintswarningsaserrors;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.apollographql.apollo.response.CustomTypeAdapter;
import com.apollographql.apollo.response.CustomTypeValue;
import com.webiphany.LinksQuery;
import com.webiphany.type.CustomType;
// import com.webiphany.type.CustomType;

import java.util.List;

import javax.annotation.Nonnull;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        status = findViewById(R.id.status);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runApolloQuery();
            }
        });
    }

    private void runApolloQuery()
    {
        OkHttpClient okHttpClient = new OkHttpClient();

        status.setText("Preparing to load data");

        final String BASE_URL = "http://10.252.181.141:3000/graphql";

        CustomTypeAdapter<String> customTypeAdapter = new CustomTypeAdapter<String>() {
            @Override
            public String decode(CustomTypeValue value) {
                return value.value.toString();
            }

            @Override
            public CustomTypeValue<?> encode(String value) {
                return new CustomTypeValue.GraphQLString(value);
            }
        };

        ApolloClient apolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .addCustomTypeAdapter(CustomType.URL, customTypeAdapter)
                .build();

        final LinksQuery links = LinksQuery.builder().build();

        ApolloCall<LinksQuery.Data> linksQuery =
                apolloClient.query(links);

        status.setText("Queueing request");

        linksQuery.enqueue(new ApolloCall.Callback<LinksQuery.Data>() {

            @Override public void onResponse(@Nonnull Response<LinksQuery.Data> dataResponse) {
                LinksQuery.Data data = dataResponse.data();
                Log.d("GraphQL Apollo Client", data.toString());

                final List<String> links = data.links();
                if (links != null)
                {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayout ll = findViewById(R.id.linear_layout_for_links);

                            ll.removeAllViews();

                            for( int i = 0; i < links.size(); i++ ) {
                                TextView textView = new TextView(getApplicationContext());
                                String link = links.get(i);
                                Log.v("Graphql", "Link is : " + link);
                                textView.setText(link);
                                ll.addView(textView);
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(@Nonnull final ApolloException e)
            {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        status.setText("Error: " + e.getLocalizedMessage());
                    }
                });
            }
        });
    }

}
