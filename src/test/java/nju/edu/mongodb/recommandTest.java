package nju.edu.mongodb;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.Word2VecTrainer;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;

import edu.nju.util.StringMatch;
import edu.nju.util.UserCF;

public class recommandTest {
	
	@Test
	public void recommand() {
		UserCF cf = new UserCF();
		List<String> lists = cf.calculate(new String[][] {{"A","a","b","d"},{"B","a","c"},{"C","b","e"},{"D","c","d","e"}});
		for(String str: lists) {
			System.out.println(str);
		}
	}
	
	private static final String TRAIN_FILE_NAME = "./data/搜狗文本分类语料库已分词.txt";
	private static final String MODEL_FILE_NAME = "./data/搜狗.txt";
	
	String one = "进入英语课程界面，选择高级，点击评价，英语课程应该按照评价从高到低进行排序，但是出现评价高的出现在评价低的后面。";
	String two = "成功登录应用进入英语课程界面，选择该界面右上角的类型为听力的课程，结果只有部分听力课程有五星图标和评价数显示，还有一部分课程没有五星和评价数显示，见截图2";
	
	@Test
	public void Han() {
		try {
			WordVectorModel wordVectorModel = trainOrLoadModel();
			DocVectorModel docVectorModel = new DocVectorModel(wordVectorModel);
			System.out.println(docVectorModel.similarity(one, two));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void Old() {
		StringMatch match = new StringMatch();
		System.out.println(match.score(match.Ansj(one), match.Ansj(two)));
	}
	
	static WordVectorModel trainOrLoadModel() throws IOException
    {
        if (!IOUtil.isFileExisted(MODEL_FILE_NAME))
        {
            if (!IOUtil.isFileExisted(TRAIN_FILE_NAME))
            {
                System.err.println("语料不存在，请阅读文档了解语料获取与格式：https://github.com/hankcs/HanLP/wiki/word2vec");
                System.exit(1);
            }
            Word2VecTrainer trainerBuilder = new Word2VecTrainer();
            return trainerBuilder.train(TRAIN_FILE_NAME, MODEL_FILE_NAME);
        }

        return loadModel();
    }
	
	static WordVectorModel loadModel() throws IOException
    {
        return new WordVectorModel(MODEL_FILE_NAME);
    }
}
