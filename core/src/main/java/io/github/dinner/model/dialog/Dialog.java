package io.github.dinner.model.dialog;

import java.util.HashMap;
import java.util.Map;

public class Dialog {
    private static final int CHARS_LIMIT = 54;
	private Map<Integer, DialogNode> nodes = new HashMap<Integer, DialogNode>();

	public DialogNode getNode(int id) {
		return nodes.get(id);
	}

	public void addNode(DialogNode node) {
		this.nodes.put(node.getID(), node);
	}

	public int getStart() {
		return 0;
	}

	public int size() {
		return nodes.size();
	}



	public void removeNode(int id) {
		DialogNode previousNode = nodes.get(id - 1);
		previousNode.getPointers().remove((Integer) id);
		nodes.remove(id);

	}

    public static String generateDialog(String text)
    {
        String[] words = text.split(" ");
        StringBuilder formattedDialog = new StringBuilder();
        int charsCounter = 0;

        for(int i = 0; i < words.length; i++)
        {
            charsCounter += words[i].length()+1;
            if(charsCounter <= CHARS_LIMIT)
            {
                formattedDialog.append(words[i]).append(" ");
            }
            else
            {
                formattedDialog.append("\n").append(words[i]).append(" ");
                charsCounter = words[i].length()+1;
            }
        }
        return formattedDialog.toString();
    }



}
