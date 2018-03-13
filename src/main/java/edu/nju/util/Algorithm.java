package edu.nju.util;

import java.util.List;

import edu.nju.entities.BugMirror;

public interface Algorithm {
	public List<BugMirror> sort(BugMirror sample, List<BugMirror> lists);
	public List<BugMirror> sort(List<BugMirror> lists);
}
