# SolarexButterKnifeDemo
自制编译时IoC框架SolarexButterKnife

Usage:
```
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv)
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InjectView.bind(this);
        Log.d("Solarex", "onCreate: tv = " + tv );
    }
}
```