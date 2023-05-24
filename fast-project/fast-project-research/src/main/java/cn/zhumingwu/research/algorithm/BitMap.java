package cn.zhumingwu.research.algorithm;


import lombok.extern.slf4j.Slf4j;


/*
 *  Breadth-First Search
 */
@Slf4j
public class BitMap {

    private int[] bitmap = new int[1];
    private int bit = 32;

    public boolean set(int number) {
        int index = number / bit;
        int position = number % bit;
        if (index > this.bitmap.length) {
            int[] newOne = new int[index];
            for (int i = 0; i < bitmap.length; i++) {
                newOne[i] = this.bitmap[i];
            }
            this.bitmap = newOne;
        }
        if (this.contains(index, position)) {
            return false;
        }
        bitmap[index] |= 1 << (bit - 1 - position);
        return true;
    }
   public boolean containsDuplicate(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            if (!this.set(nums[i])) {
                return true;
            }
        }
        return false;
    }
    private boolean contains(int index, int position) {

        return (bitmap[index] & (1 << (bit - 1 - position))) != 0;
    }
}
