ASNETutorial    [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ASNETutorial-brightgreen.svg?style=flat)](https://android-arsenal.com/details/3/921)
============

Пример применения библиотеки [ASNE](https://github.com/gorbin/ASNE)

Во время разработки приложений на Андроид часта встает вопрос интеграции соц сетей в приложение - логин через социальную сеть, рассказать друзьям о приложении, просмотреть список друзей в приложении. Существует множество способов сделать это: 
Подключить SDK или API социальной сети 
Использование же oAuth запросов, но это не дает использовать возможности интеграции приложения с уже установленными приложениями социальных сетей - большинство пользователей социальных сетей используют и мобильные приложения
Необходимо потратить время и нервы чтобы разобраться как использовать ту или иную социальную сеть, не говоря уже об ошибках или ограничениях от которых хочется вырвать все волосы на голове.

//слишком рекламно
А что если в уже давно рабочий проект необходимо добавить поддержку еще одной социальной сети? Иногда для этого потребуется переделать всю интеграцию социальных сетей, но отсутствие времени может привести к созданию велосипедов. В идеале неплохо бы создать общий интерфейс для работы со всеми социальными сетями. Для этого можно воспользоваться модулями библиотеки [ASNE](https://github.com/gorbin/ASNE). 
Используя модуль библиотеки вы подключите SDK или API выбранной социальной сети и интерфейс для наиболее частых используемых [запросов к ней](https://github.com/gorbin/ASNE/wiki/SocialNetwork-methods) тем самым сэкономив время и упростив добавление другой социальной сети. Библиотека так же позволяет просто использовать методы SDK социальной сети и предоставляет токены для составления запросов к ним. 
Добавить социальную сеть как модуль, если ее нет в разработанных не составит труда - это легко сделать по аналогии с любым другим модулем. 

В данной статье я покажу как можно легко добавить поддержку VK и Odnoklassniki в андроид приложении используя соответсвующие [модули ASNE](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.asne%22). Это упрощенный пример включающий добавления логина, шаринга ссылки и вывода списка друзей.

##Регистрация приложения в социальной сети
Для добавления социальной сети в ваше приложение потребуется ключ для совершения запросов. Поэтому первым шагом необходимо зарегистрировать приложение - по ссылкам вы увидите краткое руководство по созданию приложения для

 - [VK](https://github.com/gorbin/ASNE/wiki/Create-Vkontakte-App)
 - [Odnoklassniki](https://github.com/gorbin/ASNE/wiki/Create-Odnoklassniki-App)

для продолжения вам необходимы ключи, которые используются в SDK социальных сетей 
- VK App ID 
- Ok App ID
- Ok публичный ключ
- Ok секретный ключ


##Интеграция ВК и Одноклассники в приложение

1. Создайте новый проект
2. Сохраните ключи в `values/strings.xml`
    	
    **strings.xml**([source](https://github.com/gorbin/ASNETutorial/blob/ru/app/src/main/res/values/strings.xml))
     ```xml
    <?xml version="1.0" encoding="utf-8"?>
        <resources>
            <string name="app_name">ASNE-tutorial</string>
        
            <string name="vk_app_id">4542602</string>

			<string name="ok_app_id">1096125440</string>
			<string name="ok_public_key">CBANIGFCEBABABABA</string>
			<string name="ok_secret_key">FF5161844C04525B64FA41A7</string>
        </resources>
    ```	
3. Добавим разрешения и активити для интегарции с Одноклассниками - откроем `AndroidManifest.xml` и добавим uses-permission - INTERNET, ACCESS_NETWORK_STATE и добавим ru.ok.android.sdk.OkAuthActivity
    
    **AndroidManifest.xml**([код](https://github.com/gorbin/ASNETutorial/blob/ru/app/src/main/AndroidManifest.xml))
    ```xml
    <?xml version="1.0" encoding="utf-8"?>
	<manifest xmlns:android="http://schemas.android.com/apk/res/android"
		package="com.github.gorbin.asnetutorial" >

		<uses-permission android:name="android.permission.INTERNET" />
		<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>

		<application
			android:allowBackup="true"
			android:icon="@drawable/ic_launcher"
			android:label="@string/app_name"
			android:theme="@style/AppTheme" >
			<activity
				android:name=".MainActivity"
				android:label="@string/app_name" >
				<intent-filter>
					<action android:name="android.intent.action.MAIN" />
					<category android:name="android.intent.category.LAUNCHER" />
				</intent-filter>
			</activity>

			<activity
				android:name="ru.ok.android.sdk.OkAuthActivity"
				android:launchMode="singleTask"
				android:configChanges="orientation">
				<intent-filter>
					<action android:name="android.intent.action.VIEW" />

					<category android:name="android.intent.category.DEFAULT" />
					<category android:name="android.intent.category.BROWSABLE" />

					<data
						android:host="ok1096125440"
						android:scheme="okauth" />
				</intent-filter>
			</activity>
		</application>


	</manifest>
    ```
4. Добавим зависимости для [модулей ASNE](https://github.com/gorbin/ASNE):
    
    Например в Android Studio Откойте _Project Structure_ => выберите модуль приложения и откройте _Dependencies_ => _Add new library dependency_

 ![add library dependecy](http://i.imgur.com/4k62Ux1.png)
    
 Затем по запросу `asne` вы увидите все модули доступные в библиотеке и добавьте в зависимость **asne-vk, asne-odnoklassniki**
    
 ![search asne](http://i.imgur.com/gYou0Uf.png)
    
 либо вручную добавьте зависимоси в `build.gradle`
    
 **build.gradle**([код](https://github.com/gorbin/ASNETutorial/blob/ru/app/build.gradle))
    
 ```
 apply plugin: 'com.android.application'

 android {
     compileSdkVersion 19
     buildToolsVersion '20.0.0'
 
     defaultConfig {
         applicationId "com.github.gorbin.asnetutorial"
         minSdkVersion 10
         targetSdkVersion 19
         versionCode 1
         versionName "1.0"
     }
     buildTypes {
         release {
             runProguard false
             proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
         }
     }
 }

 dependencies {
     compile fileTree(include: ['*.jar'], dir: 'libs')
     compile 'com.android.support:appcompat-v7:20.0.0'
     compile 'com.github.asne:asne-vk:0.2.1'
     compile 'com.github.asne:asne-odnoklassniki:0.2.1'
 }
 ```
5. Теперь немного украсим наше приложение
  В главном фрагменте расположим 2 кнопки отвечающие за логин в социальные сети
 **main_fragment.xml**([код](https://github.com/gorbin/ASNETutorial/blob/ru/app/src/main/res/layout/main_fragment.xml))
    ```xml
    <?xml version="1.0" encoding="utf-8"?>

	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
		android:orientation="vertical" android:layout_width="match_parent"
		android:layout_height="match_parent"
		android:background="#FFCCCCCC">

		<Button
			android:layout_width="fill_parent"
			android:layout_height="wrap_content"
			android:text="Login via VK"
			android:id="@+id/vk"
			android:layout_gravity="center_horizontal"
			android:background="@color/vk"
			android:layout_margin="8dp"
			android:textColor="#ffffffff" />
		<Button
			android:layout_width="fill_parent"
			android:layout_height="wrap_content"
			android:text="Login via Odnoklassniki"
			android:id="@+id/ok"
			android:layout_gravity="center_horizontal"
			android:background="@color/ok"
			android:layout_margin="8dp"
			android:textColor="#ffffffff"/>
	</LinearLayout>
    ```
 Зададим внешний вид профиля пользователя
    **profile_fragment.xml**(full [source](https://github.com/gorbin/ASNETutorial/blob/ru/app/src/main/res/layout/profile_fragment.xml))
    ```xml
    <?xml version="1.0" encoding="utf-8"?>

	<ScrollView
		xmlns:android="http://schemas.android.com/apk/res/android"
		android:layout_width="fill_parent"
		android:layout_height="fill_parent"
		android:background="@color/grey_light">

		<RelativeLayout
			android:layout_width="wrap_content"
			android:layout_height="wrap_content"
			android:layout_alignParentTop="true"
			android:layout_alignParentLeft="true"
			android:layout_alignParentStart="true"
			android:layout_margin="8dp"
			android:id="@+id/frame"
			android:background="@color/dark">

			<RelativeLayout
				android:layout_width="fill_parent"
				android:layout_height="wrap_content"
				android:layout_alignParentTop="true"
				android:layout_alignParentLeft="true"
				android:layout_alignParentStart="true"
				android:layout_margin="3dp"
				android:id="@+id/card"
				android:background="#FFFFFF">

				<ImageView
					android:layout_width="100dp"
					android:layout_height="100dp"
					android:id="@+id/imageView"
					android:layout_margin="8dp"
					android:padding="2dp"
					android:background="@color/grey_light"
					android:layout_alignParentTop="true"
					android:layout_alignParentLeft="true"
					android:layout_alignParentStart="true"
					android:src="@drawable/user"
					android:adjustViewBounds="true"
					android:cropToPadding="true"
					android:scaleType="centerCrop"/>

				<TextView
					android:layout_width="wrap_content"
					android:layout_height="wrap_content"
					android:textAppearance="?android:attr/textAppearanceLarge"
					android:text="NoName"
					android:maxLines="3"
					android:singleLine="false"
					android:id="@+id/name"
					android:padding="8dp"
					android:layout_alignTop="@+id/imageView"
					android:layout_toRightOf="@+id/imageView"
					android:layout_toEndOf="@+id/imageView"
					android:layout_alignParentRight="true"
					android:layout_alignParentEnd="true" />

				<TextView
					android:layout_width="wrap_content"
					android:layout_height="wrap_content"
					android:text="null"
					android:maxLines="3"
					android:singleLine="false"
					android:id="@+id/id"
					android:padding="8dp"
					android:layout_below="@+id/name"
					android:layout_alignLeft="@+id/name"
					android:layout_alignStart="@+id/name" />

				<TextView
					android:layout_width="wrap_content"
					android:layout_height="wrap_content"
					android:text=""
					android:id="@+id/info"
					android:padding="8dp"
					android:layout_marginBottom="4dp"
					android:layout_below="@+id/imageView"
					android:layout_alignParentLeft="true"
					android:layout_alignParentStart="true" />

			</RelativeLayout>
			<LinearLayout
				android:layout_width="match_parent"
				android:layout_height="wrap_content"
				android:id="@+id/buttonLayout"
				android:layout_below="@+id/card"
				android:layout_alignParentLeft="true"
				android:layout_alignParentRight="true"
				android:gravity="center"
				android:background="@color/grey_light">

				<Button
					android:layout_width="match_parent"
					android:layout_height="match_parent"
					android:text="Friends"
					android:id="@+id/friends"
					android:padding="8dp"
					android:background="@color/dark"
					android:layout_marginRight="1dp"
					android:layout_weight="1"
					android:textColor="#ffffffff"/>
				<Button
					android:layout_width="match_parent"
					android:layout_height="match_parent"
					android:text="Share"
					android:id="@+id/share"
					android:padding="8dp"
					android:background="@color/dark"
					android:layout_weight="1"
					android:textColor="#ffffffff"/>
			</LinearLayout>
		</RelativeLayout>

	</ScrollView>
    ```

 и сохраним цвета социальных сетей

 **color.xml**(full [source](https://github.com/gorbin/ASNETutorial/blob/ru/app/src/main/res/values/colors.xml))
    ```xml
    <?xml version="1.0" encoding="utf-8"?>
	<resources>
		<color name="grey_light">#FFCCCCCC</color>
		<color name="dark">#4b4b4b</color>

		<color name="vk">#36638e</color>
		<color name="ok">#cf6700</color>
	</resources>
    ```
    
6. Теперь в `MainActivity.java` необходимо в `onActivityResult` ловить ответ после запроса логина

    **MainActivity.java**(full [source](https://github.com/gorbin/ASNETutorial/blob/ru/app/src/main/java/com/github/gorbin/asnetutorial/MainActivity.java))
    
 ```java
    public static final String SOCIAL_NETWORK_TAG = "SocialIntegrationMain.SOCIAL_NETWORK_TAG";
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(SOCIAL_NETWORK_TAG);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
 ```
 При обработке запроса логина социальная сеть отправляет `onActivityResult` проверяем его отправляем в `SocialNetworkManager` который передаст его в соответствующую `SocialNetwork`
 
7. Теперь интегрируем социальную сеть в `MainFragment.java` - это просто:

    * Достанем ключи социальных сетей из `values.xml`
    
    ```java
    String VK_KEY = getActivity().getString(R.string.vk_app_id);
    String OK_APP_ID = getActivity().getString(R.string.ok_app_id);
    String OK_PUBLIC_KEY = getActivity().getString(R.string.ok_public_key);
    String OK_SECRET_KEY = getActivity().getString(R.string.ok_secret_key);
    ```
	
	* Получим `SocialNetworkManager`
    
    ```java
    mSocialNetworkManager = (SocialNetworkManager) getFragmentManager().findFragmentByTag(MAinActivity.SOCIAL_NETWORK_TAG);
    ```
    
    * Создадим `SocialNetworks` с соответствующими разрешениями
    
    ```java
    String[] vkScope = new String[] {
        VKScope.FRIENDS,
        VKScope.WALL,
		VKScope.PHOTOS,
        VKScope.NOHTTPS,
        VKScope.STATUS,
    };
    VkSocialNetwork vkNetwork = new VkSocialNetwork(this, VK_KEY, vkScope);

    String[] okScope = new String[] {
        OkScope.VALUABLE_ACCESS
    };
    OkSocialNetwork okNetwork = new OkSocialNetwork(this, OK_APP_ID, OK_PUBLIC_KEY, OK_SECRET_KEY, okScope);
        
    ```
    
    * Проверим существует ли `SocialNetworkManager` 
	 * Если не существует зададим его и добавим в него `SocialNetworks`
    
    ```java
    mSocialNetworkManager = new SocialNetworkManager();
    
    mSocialNetworkManager.addSocialNetwork(vkNetwork);
    mSocialNetworkManager.addSocialNetwork(okNetwork);
    
    //Initiate every network from mSocialNetworkManager
    getFragmentManager().beginTransaction().add(mSocialNetworkManager, MAinActivity.SOCIAL_NETWORK_TAG).commit();
    mSocialNetworkManager.setOnInitializationCompleteListener(this);
    ```
    не забудьте добавить `SocialNetworkManager.OnInitializationCompleteListener`
     
     * Если `SocialNetworkManager` существует - задали в activity или другом фрагменте - выберем все инициализированные социальные сети и установим им `OnLoginCompleteListener`
    
    ```java
    if(!mSocialNetworkManager.getInitializedSocialNetworks().isEmpty()) {
        List<SocialNetwork> socialNetworks = mSocialNetworkManager.getInitializedSocialNetworks();
        for (SocialNetwork socialNetwork : socialNetworks) {
            socialNetwork.setOnLoginCompleteListener(this);
        }
    ```
    * Теперь необходимо обработать callback инициации `SocialNetworks`
    
    ```java
    @Override
    public void onSocialNetworkManagerInitialized() {
        for (SocialNetwork socialNetwork : mSocialNetworkManager.getInitializedSocialNetworks()) {
            socialNetwork.setOnLoginCompleteListener(this);
            initSocialNetwork(socialNetwork);
        }
    }
    ```
    не забудьте добавить `OnLoginCompleteListener`
    
 Весь исходный код `onCreateView` и `onSocialNetworkManagerInitialized` из `MainFragment` инициацией социальных сетей и установкой listenerов
    
    **MainFragment.java**(full [source](https://github.com/gorbin/ASNETutorial/blob/ru/app/src/main/java/com/github/gorbin/asnetutorial/MainFragment.java))
    
    ```java
    public static SocialNetworkManager mSocialNetworkManager;
    /**
     * SocialNetwork Ids in ASNE:
     * 1 - Twitter
     * 2 - LinkedIn
     * 3 - Google Plus
     * 4 - Facebook
     * 5 - Vkontakte
     * 6 - Odnoklassniki
     * 7 - Instagram
     */
    public static final int VK = 5;
    public static final int OK = 6;
    private Button vk;
    private Button ok;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);
        // init buttons and set Listener
        vk = (Button) rootView.findViewById(R.id.vk);
        vk.setOnClickListener(loginClick);
        ok = (Button) rootView.findViewById(R.id.ok);
        ok.setOnClickListener(loginClick);

        //Get Keys for initiate SocialNetworks
        String VK_KEY = getActivity().getString(R.string.vk_app_id);
        String OK_APP_ID = getActivity().getString(R.string.ok_app_id);
        String OK_PUBLIC_KEY = getActivity().getString(R.string.ok_public_key);
        String OK_SECRET_KEY = getActivity().getString(R.string.ok_secret_key);

        //Chose permissions
        String[] okScope = new String[] {
                OkScope.VALUABLE_ACCESS
        };
        String[] vkScope = new String[] {
                VKScope.FRIENDS,
                VKScope.WALL,
                VKScope.PHOTOS,
                VKScope.NOHTTPS,
                VKScope.STATUS,
        };

        //Use manager to manage SocialNetworks
        mSocialNetworkManager = (SocialNetworkManager) getFragmentManager().findFragmentByTag(MainActivity.SOCIAL_NETWORK_TAG);

        //Check if manager exist
        if (mSocialNetworkManager == null) {
            mSocialNetworkManager = new SocialNetworkManager();

            //Init and add to manager VkSocialNetwork
            VkSocialNetwork vkNetwork = new VkSocialNetwork(this, VK_KEY, vkScope);
            mSocialNetworkManager.addSocialNetwork(vkNetwork);

            //Init and add to manager OkSocialNetwork
            OkSocialNetwork okNetwork = new OkSocialNetwork(this, OK_APP_ID, OK_PUBLIC_KEY, OK_SECRET_KEY, okScope);
            mSocialNetworkManager.addSocialNetwork(okNetwork);

            //Initiate every network from mSocialNetworkManager
            getFragmentManager().beginTransaction().add(mSocialNetworkManager, MainActivity.SOCIAL_NETWORK_TAG).commit();
            mSocialNetworkManager.setOnInitializationCompleteListener(this);
        } else {
            //if manager exist - get and setup login only for initialized SocialNetworks
            if(!mSocialNetworkManager.getInitializedSocialNetworks().isEmpty()) {
                List<SocialNetwork> socialNetworks = mSocialNetworkManager.getInitializedSocialNetworks();
                for (SocialNetwork socialNetwork : socialNetworks) {
                    socialNetwork.setOnLoginCompleteListener(this);
                    initSocialNetwork(socialNetwork);
                }
            }
        }
        return rootView;
    }

    private void initSocialNetwork(SocialNetwork socialNetwork){
        if(socialNetwork.isConnected()){
            switch (socialNetwork.getID()){
                case VK:
                    vk.setText("Show VK profile");
                    break;
                case OK:
                    ok.setText("Show Odnoklassniki profile");
                    break;
            }
        }
    }
    @Override
    public void onSocialNetworkManagerInitialized() {
        //when init SocialNetworks - get and setup login only for initialized SocialNetworks
        for (SocialNetwork socialNetwork : mSocialNetworkManager.getInitializedSocialNetworks()) {
            socialNetwork.setOnLoginCompleteListener(this);
            initSocialNetwork(socialNetwork);
        }
    }
    ```
![MainFragment](http://imgur.com/i22fMz3.png)
    
8. Запросим логин в каждой социальной сети
    
    ```java
    SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(networkId);
    socialNetwork.requestLogin();   
    ```
    
 Весь исходный код `OnClickListener` loginClick с проверкой состояния подключения к социальной сети, если социальная сеть подключена - откроем `ProfileFragment.java`
 
     **MainFragment.java**([код](https://github.com/gorbin/ASNETutorial/blob/ru/app/src/main/java/com/github/gorbin/asnetutorial/MainFragment.java))

    ```java
    private View.OnClickListener loginClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int networkId = 0;
            switch (view.getId()){
                case R.id.vk:
                    networkId = VK;
                    break;
                case R.id.ok:
                    networkId = OK;
                    break;
            }
            SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(networkId);
            if(!socialNetwork.isConnected()) {
                if(networkId != 0) {
                    socialNetwork.requestLogin();
                    MainActivity.showProgress("Loading social person");
                } else {
                    Toast.makeText(getActivity(), "Wrong networkId", Toast.LENGTH_LONG).show();
                }
            } else {
                startProfile(socialNetwork.getID());
            }
        }
    };
    
    ```

9. После заполнения логина или обработки логина приложением социальной сети получим `onLoginSuccess(int networkId)` или `onError(int networkId, String requestID, String errorMessage, Object data)` - выведем соответствующее сообщение 
    
 **MainFragment.java**([код](https://github.com/gorbin/ASNETutorial/blob/ru/app/src/main/java/com/github/gorbin/asnetutorial/MainFragment.java))

    ```java
    @Override
    public void onLoginSuccess(int networkId) {
        MainActivity.hideProgress();
		Toast.makeText(getActivity(), "Login Success", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(int networkId, String requestID, String errorMessage, Object data) {
        MainActivity.hideProgress();
        Toast.makeText(getActivity(), "ERROR: " + errorMessage, Toast.LENGTH_LONG).show();
    }   
    ```
	
10. Откроем `ProfileFragment.java` с помощью метода:

 **MainFragment.java**([код](https://github.com/gorbin/ASNETutorial/blob/ru/app/src/main/java/com/github/gorbin/asnetutorial/MainFragment.java))

    ```java
    private void startProfile(int networkId){
        ProfileFragment profile = ProfileFragment.newInstannce(networkId);
        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack("profile")
                .replace(R.id.container, profile)
                .commit();
    } 
    ```
	
11. В `ProfileFragment.java` получим идетификатор социальной сети из `MainFragment.java`  

 **ProfileFragment.java**([код](https://github.com/gorbin/ASNETutorial/blob/ru/app/src/main/java/com/github/gorbin/asnetutorial/ProfileFragment.java))
    ```java
    public static ProfileFragment newInstannce(int id) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(NETWORK_ID, id);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        networkId = getArguments().containsKey(NETWORK_ID) ? getArguments().getInt(NETWORK_ID) : 0;
        
    }
    ```
    
12. Теперь с помощью `networkId` мы выберем социальную сеть и запросим данные профиля текущего пользователя следующим образом:

    ```java
    socialNetwork = MainFragment.mSocialNetworkManager.getSocialNetwork(networkId);
    socialNetwork.setOnRequestCurrentPersonCompleteListener(this);
    socialNetwork.requestCurrentPerson();
    ```
 не забудьте добавить `OnRequestSocialPersonCompleteListener` 
13. После обработки запроса мы можеи использовать полученный объект `SocialPerson` для заполнения профиля пользователя в приложении, либо вывести ошибку при неудаче

 **ProfileFragment.java**(full [source](https://github.com/gorbin/ASNETutorial/blob/ru/app/src/main/java/com/github/gorbin/asnetutorial/ProfileFragment.java))
    ```java
    @Override
    public void onRequestSocialPersonSuccess(int i, SocialPerson socialPerson) {
        MainActivity.hideProgress();
        name.setText(socialPerson.name);
        id.setText(socialPerson.id);
        String socialPersonString = socialPerson.toString();
        String infoString = socialPersonString.substring(socialPersonString.indexOf("{")+1, socialPersonString.lastIndexOf("}"));
        info.setText(infoString.replace(", ", "\n"));
        Picasso.with(getActivity())
            .load(socialPerson.avatarURL)
            .into(photo);
    }
    
    @Override
    public void onError(int networkId, String requestID, String errorMessage, Object data) {
        MainActivity.hideProgress();
        Toast.makeText(getActivity(), "ERROR: " + errorMessage, Toast.LENGTH_LONG).show();
    }
    ```
![MainFragment](http://imgur.com/b9c0VZr.png)    
14. Для выхода из социальной сети необходимо использовать метод `logout()` 
 ```java
 socialNetwork.logout();
 getActivity().getSupportFragmentManager().popBackStack();
 ```
15. И честно говоря это все - добавили ВК и Одноклассники в приложение. Аналогично вы можете добавить и другие социальные сети Facebook, Twitter, Linkedin, Instagram или Google Plus лишь добавив соответствующую зависимость и добавив их в `SocialNetworkManager` как в шаге 8:
 ```java
    FacebookSocialNetwork fbNetwork = new FacebookSocialNetwork(this, fbScope);
    mSocialNetworkManager.addSocialNetwork(fbNetwork);

    TwitterSocialNetwork twNetwork = new TwitterSocialNetwork(this, TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
    mSocialNetworkManager.addSocialNetwork(twNetwork);

    LinkedInSocialNetwork liNetwork = new LinkedInSocialNetwork(this, LINKEDIN_CONSUMER_KEY, LINKEDIN_CONSUMER_SECRET, linkedInScope);
    mSocialNetworkManager.addSocialNetwork(liNetwork);

    GooglePlusSocialNetwork gpNetwork = new GooglePlusSocialNetwork(this);
    mSocialNetworkManager.addSocialNetwork(gpNetwork);
	
	InstagramSocialNetwork instagramNetwork = new InstagramSocialNetwork(this, INSTAGRAM_CLIENT_KEY, INSTAGRAM_CLIENT_SECRET, instagramScope);
    mSocialNetworkManager.addSocialNetwork(instagramNetwork);
 ```
 И конечно же вы можете использовать выше описанные методы для раобты с ними
 
14. Но давайте разберем еще несколько запросов **Share link** и **Get user friendslist**
 
 Давайте **share** ссылку с помощью социальной сети:
 * Настройим кнопку
 
     ```java
     share = (Button) rootView.findViewById(R.id.share);
     share.setOnClickListener(shareClick);
     ```

 * Для отправки ссылки на стену пользователя нам необходимо ее передать в Bundle

      ```java
      Bundle postParams = new Bundle();
      postParams.putString(SocialNetwork.BUNDLE_LINK, link);
      socialNetwork.requestPostLink(postParams, message, postingComplete);
      ```
 * И конечно же обработать ответы

        ```java
        private OnPostingCompleteListener postingComplete = new OnPostingCompleteListener() {
            @Override
            public void onPostSuccessfully(int socialNetworkID) {
                Toast.makeText(getActivity(), "Sent", Toast.LENGTH_LONG).show();
            }
    
            @Override
            public void onError(int socialNetworkID, String requestID, String errorMessage, Object data) {
                Toast.makeText(getActivity(), "Error while sending: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        };
        ```
 * Итак в `OnClickListener shareClick` покажем пользователю простой диалог в котором спросим хочет ли он расшарить ссылку и если да отправим ее
 
        **ProfileFragment.java**(full [source](https://github.com/gorbin/ASNETutorial/blob/ru/app/src/main/java/com/github/gorbin/asnetutorial/ProfileFragment.java))
      ```java
        private View.OnClickListener shareClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder ad = alertDialogInit("Would you like to post Link:", link);
            ad.setPositiveButton("Post link", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Bundle postParams = new Bundle();
                    postParams.putString(SocialNetwork.BUNDLE_LINK, link);
                    socialNetwork.requestPostLink(postParams, message, postingComplete);
                }
            });
            ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.cancel();
                }
            });
            ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    dialog.cancel();
                }
            });
            ad.create().show();
        }
    };
      ```
      
  ![Share](http://imgur.com/DX5oj68.png)

  Теперь выведем список друзей пользователя:   
  * Получим `SocialNetwork` из идентификатора социальной сети запросим список друзей

        ```java
        SocialNetwork socialNetwork = MainFragment.mSocialNetworkManager.getSocialNetwork(networkId);
        socialNetwork.setOnRequestGetFriendsCompleteListener(this);
        socialNetwork.requestGetFriends();
        ```
    не забудьте добавить `OnRequestGetFriendsCompleteListener` 
 * Обработаем ответ

        ```java
        @Override
        public void OnGetFriendsIdComplete(int id, String[] friendsID) {
            ((MainActivity)getActivity()).getSupportActionBar().setTitle(friendsID.length + " Friends");
        }
    
        @Override
        public void OnGetFriendsComplete(int networkID, ArrayList<SocialPerson> socialPersons) {
            MainActivity.hideProgress();
            FriendsListAdapter adapter = new FriendsListAdapter(getActivity(), socialPersons, networkID);
            listView.setAdapter(adapter);
        }
    
        @Override
        public void onError(int networkId, String requestID, String errorMessage, Object data) {
            MainActivity.hideProgress();
            Toast.makeText(getActivity(), "ERROR: " + errorMessage, Toast.LENGTH_LONG).show();
        }
        ```

 ![Friends](http://imgur.com/VvOfgAN.png)
 
 Подробнее вы можете изучит в [**FriendsFragment.java**](https://github.com/gorbin/ASNETutorial/blob/ru/app/src/main/java/com/github/gorbin/asnetutorial/FriendsFragment.java)

##Итог
Используя модули библиотеки ASNE вы легко и быстро интегрируете любую популярную социальную сеть в приложение. Конечно же в библиотеке содержится [больше методов](https://github.com/gorbin/ASNE/wiki/SocialNetwork-methods) которые возможно пригодятся в вашем приложении. А в случае необходимости использовать методы SDK вы можете получить токен или объект SDK и написать свой метод

Если данное приложение вам показалось простым вы можете посмотерть реализацию всех методов - [тут](https://github.com/gorbin/ASNE)

Код проекта: 
[GitHub](https://github.com/gorbin/ASNETutorial)
[Zip](https://github.com/gorbin/ASNETutorial/archive/master.zip)
    
Статья по подключению Facebook, Twitter and LinkedIn на [codeproject.com](http://www.codeproject.com/Articles/815900/Android-social-network-integration)    

В данный момент библиотека дорабатывается и я буду рад вашим советам или помощи в разработке.
В ближайшее время напишу статью по подключению социальных сетей как модулей и подключу азиатскую социальную сеть. А так же все не доходят руки до javdoc и тестов
    
    
