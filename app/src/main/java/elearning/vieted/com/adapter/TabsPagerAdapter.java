package elearning.vieted.com.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import elearning.vieted.com.fragment.MyCourseFragment;
import elearning.vieted.com.fragment.SettingFragment;
import elearning.vieted.com.fragment.VietEDFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
			case 0:
				// Vieted fragment activity
				return new VietEDFragment();
			case 1:
				// Games fragment activity
				return new MyCourseFragment();
			case 2:
				// Movies fragment activity
				return new SettingFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}
}
