package debug;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.login.LoginMainActivity;

/**
 * @author : yzf
 * time   : 2019/08/05
 * description：
 */
public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在这里传值给需要调试的Activity
        Intent intent = new Intent(this, LoginMainActivity.class);
        intent.putExtra("id", "9500116");
        startActivity(intent);
        finish();
    }
}

