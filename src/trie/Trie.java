package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie.
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {

	private Trie() {
	}

	/**
	 * Builds a trie by inserting all words in the input array, one at a time, in
	 * sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!) The words in the
	 * input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */

	private static ArrayList<String> words;
	private static TrieNode sor;
	private static TrieNode front;

	private static void insertWord(String word, TrieNode r) {

		String nWord = word;
		Indexes ind;
		String compareword;

		TrieNode ptr = r;

		TrieNode save = null;
		TrieNode prevA = r;
		TrieNode prevB = r;
		String num;
		int j = 0;
		TrieNode newTrieNode = null;
		int wordInd;
		short add = 0;
		short start;
		short end;
		if (ptr.substr == null) {
			words.add(nWord);

		} else {
			add = (short) (ptr.substr.endIndex + 1);
		}

		if (ptr.substr == null && ptr.firstChild == null) {
			wordInd = words.indexOf(nWord);
			start = 0;
			num = "" + nWord.length();
			end = Short.parseShort(num);
			end--;
			ind = new Indexes(wordInd, start, end);
			ptr.firstChild = new TrieNode(ind, null, null);
			return;

		} else {
			ptr = ptr.firstChild;
		}
		save = ptr;

		while (ptr != null) {

			compareword = words.get(ptr.substr.wordIndex);
			compareword = compareword.substring(ptr.substr.startIndex);

			if (nWord.charAt(j) == compareword.charAt(j)) {

				if (ptr.firstChild != null) {
					end = (short) (ptr.substr.endIndex - ptr.substr.startIndex);

					while (j != end) {
						if (word.charAt(j + 1) == compareword.charAt(j + 1)) {
							j++;
						} else {
							break;
						}
					}

					if (j < end) {
						wordInd = ptr.substr.wordIndex;
						start = add;
						num = "" + j;
						end = Short.parseShort(num);
						end = (short) (end + add);
						ind = new Indexes(wordInd, start, end);
						newTrieNode = new TrieNode(ind, null, null);
						newTrieNode.sibling = ptr.sibling;
						if (prevA.firstChild == ptr) {
							prevA.firstChild = newTrieNode;
						} else {
							prevA.sibling = newTrieNode;
						}
						ptr.substr.startIndex = (short) (j + 1);
						newTrieNode.firstChild = ptr;
						ptr.sibling = null;
						TrieNode t = newTrieNode;
						insertWord(nWord.substring(j + 1), t);
						newTrieNode.firstChild = t.firstChild;
						return;

					} else if (j == end) {
						TrieNode t = ptr;
						insertWord(nWord.substring(j + 1), t);
						ptr.firstChild = t.firstChild;
						return;
					}

				} else {
					end = ptr.substr.endIndex;
					while (j != end) {
						if (word.charAt(j + 1) == compareword.charAt(j + 1)) {
							j++;
						} else {
							break;
						}
					}

					wordInd = ptr.substr.wordIndex;
					start = add;
					num = "" + j;
					end = Short.parseShort(num);
					end = (short) (end + add);
					ind = new Indexes(wordInd, start, end);
					newTrieNode = new TrieNode(ind, null, null);

					if (prevA.firstChild == ptr) {
						prevA.firstChild = newTrieNode;

					} else {
						prevA.sibling = newTrieNode;
					}

					newTrieNode.sibling = ptr.sibling;
					ptr.substr.startIndex = (short) (newTrieNode.substr.endIndex + 1);
					ptr.sibling = null;
					newTrieNode.firstChild = ptr;
					TrieNode t = newTrieNode;
					insertWord(nWord.substring(j + 1), t);
					newTrieNode.firstChild = t.firstChild;
					return;
				}

			} else {
				prevA = ptr;
				ptr = ptr.sibling;
			}
		}

		ptr = save;

		while (ptr != null) {
			compareword = words.get(ptr.substr.wordIndex);
			compareword = compareword.substring(ptr.substr.startIndex);

			if (prevB.firstChild == ptr && nWord.compareTo(compareword) < 0) {
				wordInd = words.size() - 1;
				start = add;
				num = "" + words.get(wordInd).length();
				end = Short.parseShort(num);
				end--;
				ind = new Indexes(wordInd, start, end);
				newTrieNode = new TrieNode(ind, null, null);
				prevB.firstChild = newTrieNode;
				newTrieNode.sibling = ptr;
				return;
			} else if (prevB.firstChild != ptr && nWord.compareTo(words.get(prevB.substr.wordIndex)) > 0
					&& nWord.compareTo(compareword) < 0) {
				wordInd = words.size() - 1;
				start = add;
				num = "" + words.get(wordInd).length();
				end = Short.parseShort(num);
				end--;
				ind = new Indexes(wordInd, start, end);
				newTrieNode = new TrieNode(ind, null, null);
				prevB.sibling = newTrieNode;
				newTrieNode.sibling = ptr;
				return;
			} else {
				prevB = ptr;
				ptr = ptr.sibling;
			}
		}
		wordInd = words.size() - 1;
		start = add;
		num = "" + words.get(wordInd).length();
		end = Short.parseShort(num);
		end--;
		ind = new Indexes(wordInd, start, end);
		newTrieNode = new TrieNode(ind, null, null);
		prevB.sibling = newTrieNode;
		return;
	}

	public static TrieNode buildTrie(String[] allWords) {
		TrieNode root = new TrieNode(null, null, null);
		words = new ArrayList<String>();
		for (int w = 0; w < allWords.length; w++) {
			String word = allWords[w];
			insertWord(word, root);
		}
		TrieNode ptr = root;
		traverse(ptr);
		return root;
	}

	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf
	 * nodes in the trie whose words start with this prefix. For instance, if the
	 * trie had the words "bear", "bull", "stock", and "bell", the completion list
	 * for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell";
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and
	 * "bell", and for prefix "bell", completion would be the leaf node that holds
	 * "bell". (The last example shows that an input prefix can be an entire word.)
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be", the
	 * returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root     Root of Trie that stores all words to search on for
	 *                 completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix   Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the
	 *         prefix, order of leaf nodes does not matter. If there is no word in
	 *         the tree that has this prefix, null is returned.
	 */

	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
		TrieNode ptrNode = root;
		String temp = "";
		char a;
		char b;
		int i = 1;
		ArrayList<TrieNode> returnList = new ArrayList<TrieNode>();

		if (ptrNode.substr == null) {
			ptrNode = ptrNode.firstChild;
		} else {
			while (ptrNode != null) {
				if (ptrNode.firstChild != null) {
					TrieNode p = ptrNode.firstChild;
					returnList.addAll(completionList(p, allWords, prefix));
					ptrNode = ptrNode.sibling;
				} else {
					returnList.add(ptrNode);
					ptrNode = ptrNode.sibling;
				}
			}
			return returnList;
		}
		while (ptrNode != null) {
			temp = words.get(ptrNode.substr.wordIndex);
			temp = temp.substring(ptrNode.substr.startIndex, ptrNode.substr.endIndex + 1);
			a = temp.charAt(0);
			b = prefix.charAt(0);
			if (a == b) {
				while (i < prefix.length() && i < temp.length()) {
					if (prefix.charAt(i) != temp.charAt(i)) {
						return null;
					}
					i++;
				}
				i = 1;
				if (prefix.length() == temp.length() || prefix.length() < temp.length()) {
					if (ptrNode.firstChild != null) {
						TrieNode p = ptrNode.firstChild;
						returnList.addAll(completionList(p, allWords, prefix));
					} else {
						returnList.add(ptrNode);
					}
					break;
				} else {
					prefix = prefix.substring(ptrNode.substr.endIndex - ptrNode.substr.startIndex + 1);
					ptrNode = ptrNode.firstChild;
					continue;
				}
			}
			ptrNode = ptrNode.sibling;
		}
		if (returnList.size() == 0) {
			return null;
		}
		return returnList;

	}

	private static void sInsert(TrieNode newnode) {
		if (sor == null || sor.substr.wordIndex >= newnode.substr.wordIndex) {
			newnode.sibling = sor;
			sor = newnode;
		} else {
			TrieNode current = sor;
			while (current.sibling != null && current.sibling.substr.wordIndex < newnode.substr.wordIndex) {
				current = current.sibling;
			}
			newnode.sibling = current.sibling;
			current.sibling = newnode;
		}
	}

	private static TrieNode insertSort(TrieNode headref) {
		sor = null;
		TrieNode current = headref;
		while (current != null) {
			TrieNode next = current.sibling;
			sInsert(current);
			current = next;
		}
		front = sor;
		return front;
	}

	private static void traverse(TrieNode root) {
		if (root == null) {
			return;
		}

		while (root != null) {
			if (root.firstChild != null) {
				root.firstChild = insertSort(root.firstChild);
				traverse(root.firstChild);
			}
			root = root.sibling;
		}
	}

	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}

	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i = 0; i < indent - 1; i++) {
			System.out.print("    ");
		}

		if (root.substr != null) {
			String pre = words[root.substr.wordIndex].substring(0, root.substr.endIndex + 1);
			System.out.println("      " + pre);
		}

		for (int i = 0; i < indent - 1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}

		for (TrieNode ptr = root.firstChild; ptr != null; ptr = ptr.sibling) {
			for (int i = 0; i < indent - 1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent + 1, words);
		}
	}
}
