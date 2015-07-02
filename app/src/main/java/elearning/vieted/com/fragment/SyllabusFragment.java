package elearning.vieted.com.fragment;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import elearning.vieted.com.vietedelearning.R;
import elearning.vieted.com.adapter.ServiceHandler;

public class SyllabusFragment extends ListFragment {
    private ProgressDialog pDialog;

    // URL to get contacts JSON
    //private static String url = "http://api.androidhive.info/contacts/";

    private static String vietedURL = "http://vieted.net/c/10130/overview/demo-scorm.html?api=1&_app_id=123";

    // JSON Node names
    private static final String TAG_COURSE_STATUS = "success";
    private static final String TAG_RESULT_G = "result";
    private static final String TAG_COURSE_NAME = "name";
    private static final String TAG_COURSE_ID = "iid";
    private static final String TAG_COURSE_PRICE = "price";
    private static final String TAG_COURSE_PROGRESS = "progress";
    private static final String TAG_COURSE_STAFF = "staff";

    private static final String TAG_SYLLABUS_G= "syllabus";
    private static final String TAG_SYLLABUS_ID = "iid";

    private static final String TAG_UNIT_G = "units";
    private static final String TAG_UNIT_ID = "id";
    private static final String TAG_UNIT_NAME = "name";
    private static final String TAG_UNIT_PROGRESS = "progress";

    private static final String TAG_LESSON_G = "lessons";
    private static final String TAG_LESSON_PROGRESS = "progress";
    private static final String TAG_LESSON_STAR = "stars";
    private static final String TAG_LESSON_ID = "iid";
    private static final String TAG_LESSON_NAME = "name";

    private static final String TAG_FLOW_G = "flow";
    private static final String TAG_FLOW_NAME = "name";
    private static final String TAG_FLOW_TYPE = "type";
    private static final String TAG_FLOW_DURATION = "duration";
    private static final String TAG_FLOW_ID = "iid";
    private static final String TAG_FLOW_PROGRESS = "progress";

    // Hashmap for ListView
    List listVietEDCourse;

    public SyllabusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_syllabus, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listVietEDCourse = new ArrayList();

        new LoadVietEDSyllabus().execute();

        ListView lvSyllabus  = getListView();

        // Listview on item click listener
        lvSyllabus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String mName = ((TextView) view.findViewById(R.id.name))
                        .getText().toString();
                String mID = ((TextView) view.findViewById(R.id.iid))
                        .getText().toString();
                String mProgress = ((TextView) view.findViewById(R.id.progress))
                        .getText().toString();
                String mItemNo = ((TextView) view.findViewById(R.id.itemno))
                        .getText().toString();

                /*
                // Starting single contact activity
                Intent in = new Intent(getApplicationContext(),
                        SingleContactActivity.class);
                in.putExtra("name", mName);
                in.putExtra("iid", mID);
                in.putExtra("progress", mProgress);
                in.putExtra("itemno", mItemNo);
                startActivity(in);

                Intent inLessonActivity = new Intent(getApplicationContext(),
                        LessonListActivity.class);
                //inLessonActivity.putParcelableArrayListExtra("VietED Course", listVietEDCourse);
                */

            }
        });
        // Calling async task to get json

    }

    class LoadVietEDSyllabus extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(vietedURL, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    //Getting course status
                    String courseStatus = jsonObj.getString(TAG_COURSE_STATUS);
                    listVietEDCourse.add(courseStatus);//0

                    //Getting course information
                    JSONObject jsonCourse = jsonObj.getJSONObject(TAG_RESULT_G);

                    String courseName = jsonCourse.getString(TAG_COURSE_NAME);
                    Integer courseID = jsonCourse.getInt(TAG_COURSE_ID);
                    Integer coursePrice = jsonCourse.getInt(TAG_COURSE_PRICE);
                    Integer courseProgress=jsonCourse.getInt(TAG_COURSE_PROGRESS);
                    String courseStaff = jsonCourse.getString(TAG_COURSE_STAFF);
                    listVietEDCourse.add(courseID);//1
                    listVietEDCourse.add(courseName);//2
                    listVietEDCourse.add(courseProgress);//3
                    listVietEDCourse.add(coursePrice);//4
                    listVietEDCourse.add(courseStaff);//5


                    //Gettting course's syllabus
                    JSONObject jsonSyllabus=jsonCourse.getJSONObject(TAG_SYLLABUS_G);
                    Integer syllabusID = jsonSyllabus.getInt(TAG_SYLLABUS_ID);
                    listVietEDCourse.add(syllabusID);//6

                    //Unit
                    JSONArray jsonArrayUnit = jsonSyllabus.getJSONArray(TAG_UNIT_G);
                    for(int iU=0; iU<jsonArrayUnit.length(); iU++){
                        JSONObject jsonUnit = jsonArrayUnit.getJSONObject(iU);
                        List listUnit = new ArrayList();//7

                        String unitID = jsonUnit.getString(TAG_UNIT_ID);
                        String unitName = jsonUnit.getString(TAG_UNIT_NAME);
                        Integer unitProgress = jsonUnit.getInt(TAG_UNIT_PROGRESS);
                        listUnit.add(unitID);//0
                        listUnit.add(unitName);//1
                        listUnit.add(unitProgress);//2

                        //Lesson
                        //HashMap<String, ArrayList<HashMap<String,String>>> hmLesson
                        //        = new HashMap<String, ArrayList<HashMap<String,String>>>();
                        JSONArray jsonArrayLesson = jsonArrayUnit.getJSONObject(iU).getJSONArray(TAG_LESSON_G);
                        for(int iL=0; iL<jsonArrayLesson.length(); iL++) {
                            JSONObject jsonLesson = jsonArrayLesson.getJSONObject(iL);
                            List listLesson= new ArrayList();//3

                            Integer lessonProgress = jsonLesson.getInt(TAG_LESSON_PROGRESS);
                            Integer lessonStart = jsonLesson.getInt(TAG_LESSON_STAR);
                            Integer lessonID = jsonLesson.getInt(TAG_LESSON_ID);
                            String lessonName = jsonLesson.getString(TAG_LESSON_NAME);
                            listLesson.add(lessonID);//0
                            listLesson.add(lessonName);//1
                            listLesson.add(lessonProgress);//2
                            listLesson.add(lessonStart);//3

                            JSONArray jsonArrayFlow=jsonLesson.getJSONArray(TAG_FLOW_G);

                            //Flow (Section)
                            //ArrayList<HashMap<String,String>> alFlow = new ArrayList<HashMap<String,String>>();
                            for(int iF=0; iF<jsonArrayFlow.length(); iF++) {
                                JSONObject jsonFlow = jsonArrayFlow.getJSONObject(iF);
                                List listSection = new ArrayList();//4

                                String flowName = jsonFlow.getString(TAG_FLOW_NAME);
                                String flowType = jsonFlow.getString(TAG_FLOW_TYPE);
                                Integer flowDuration = jsonFlow.getInt(TAG_FLOW_DURATION);
                                Integer flowID = jsonFlow.getInt(TAG_FLOW_ID);
                                Integer flowProgress = jsonFlow.getInt(TAG_FLOW_PROGRESS);
                                listSection.add(flowID);
                                listSection.add(flowName);
                                listSection.add(flowProgress);
                                listSection.add(flowDuration);
                                listSection.add(flowType);

                                listLesson.add(listSection);
                            }//end for Flow

                            listUnit.add(listLesson);

                        }//end for Lesson

                        listVietEDCourse.add(listUnit);

                    }//end for Unit

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */

            ArrayList<HashMap<String, String>> arrayListUnit = new ArrayList<HashMap<String, String>>();

            for(int i=0;i<listVietEDCourse.size()-7;i++){
                List listUnit = (List)listVietEDCourse.get(i+7);

                //for(int j=0;j<listUnit.size();j++) {
                //    List listLesson = (List)listUnit.get(j);

                HashMap<String, String> hmUnit = new HashMap<String, String>();
                hmUnit.put(TAG_UNIT_ID, listUnit.get(0).toString());
                hmUnit.put(TAG_UNIT_NAME, listUnit.get(1).toString());
                hmUnit.put(TAG_UNIT_PROGRESS, listUnit.get(2).toString());
                hmUnit.put("item_no", String.valueOf(i));

                arrayListUnit.add(hmUnit);
            }
            //}

            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), arrayListUnit,
                    R.layout.list_syllabus_item, new String[] { TAG_UNIT_NAME, TAG_UNIT_ID,
                    TAG_UNIT_PROGRESS, "item_no"}, new int[] { R.id.name,
                    R.id.iid, R.id.progress, R.id.itemno});

            setListAdapter(adapter);

            ((TextView)getView().findViewById(R.id.txtCourseName)).setText(listVietEDCourse.get(2).toString());
        }
    }
}
