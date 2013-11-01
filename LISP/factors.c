#include <iostream>
#include <list>
#include <stdio.h>

int a;
int max_deep;

using namespace std;

int mul(list<int> l) 
{
	int result = 1;
	for (list<int>::iterator it = l.begin(); it != l.end(); ++it) 
	{
		result *= (*it);
	}
	return result;
}

int factors(int cur_num, list<int> l, int iter, int orig) 
{
	a++;
	if (max_deep < a) max_deep = a;
	if (iter == 0) {
		a--;
		return 0;
	} else {
		if (cur_num % iter == 0) {
			if (iter == 1) {
				if (mul(l) == orig) {
	 				list<int>::iterator it;
					cout << "(";
					for(it = l.begin(); it != l.end(); ++it) {
					cout << " "  << *it;
					}
					cout << " )" << endl;
				}
			} else {
				list<int> nlist = l;
				nlist.push_back(iter);
				factors(cur_num/iter, nlist, cur_num/iter, orig);
				
			}
		}
		list<int> nl = l;
		factors(cur_num, nl, iter - 1, orig);
		
	}
	a--;
	return 0;

}

int main(int argc, char* argv[])
{
	a = 0;
	if (argc < 2) {
		cout << "Usage: " << argv[0] << " n\n(Where n is the number you want to factorize.)" << endl;
		return 0;
	}

	list<int> l;

	int orig;
	sscanf(argv[1], "%d", &orig);
	
	factors(orig, l, orig, orig);

	cout << "The max deep is " << max_deep << endl;

	return 0;

}
